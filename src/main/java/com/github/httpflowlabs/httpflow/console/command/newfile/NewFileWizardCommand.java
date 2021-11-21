package com.github.httpflowlabs.httpflow.console.command.newfile;

import com.github.httpflowlabs.httpflow.console.command.newfile.postprocess.BodyPostProcessor;
import com.github.httpflowlabs.httpflow.console.command.newfile.postprocess.BodyPostProcessorFactory;
import com.github.httpflowlabs.httpflow.console.command.newfile.postprocess.PostProcessResult;
import com.github.httpflowlabs.httpflow.console.exception.UserCancelException;
import com.github.httpflowlabs.httpflow.console.io.SelectionDialog;
import com.github.httpflowlabs.httpflow.console.io.UserInputReader;
import com.github.httpflowlabs.httpflow.resource.impl.StringHttpFlowResource;
import com.github.httpflowlabs.httpflow.resource.parser.exception.MalformedHttpFlowException;
import com.github.httpflowlabs.httpflow.resource.parser.model.HttpFlowDocument;
import com.github.httpflowlabs.httpflow.resource.parser.model.HttpFlowHeaderValues;
import com.github.httpflowlabs.httpflow.support.HttpFlowUtils;
import org.apache.commons.lang3.StringUtils;

import java.net.URI;
import java.net.URISyntaxException;

public class NewFileWizardCommand extends NewFileCommand {

    protected String getDialogTitle() {
        return "New file wizard";
    }

    protected String getFileNameMsg() {
        return "[Step.1] Enter file name : ";
    }

    protected String getFileContents() {
        StringBuilder builder = new StringBuilder();

        String method = chooseHttpMethod(builder);
        inputUrl(builder);

        inputHeaders(builder);
        String contentType = fillContentTypeIfNeeded(builder, method);

        inputBody(builder, method, contentType);

        return builder.toString();
    }

    private String fillContentTypeIfNeeded(StringBuilder builder, String method) {
        String contentType = getContentTypeHeader(builder);
        if (isPostOrPutMethod(method) && contentType == null) {
            SelectionDialog dialog = new SelectionDialog("Content-Type is required");
            dialog.addOption("F", "application/x-www-form-urlencoded");
            dialog.addOption("J", "application/json");
            dialog.addOption("U", "application/json;charset=UTF-8");
            dialog.addOption("E", "etc (user input)");

            String type = dialog.open("httpflow> Choose 'Content-Type' header value : ");
            if ("E".equalsIgnoreCase(type)) {
                consolePrinter.print("httpflow> Enter value : ");
                contentType = UserInputReader.INSTANCE.readLine().trim();
            } else {
                contentType = dialog.getOptionMap().get(type);
            }
            if (!StringUtils.isEmpty(contentType)) {
                builder.append("Content-Type: ").append(contentType).append("\n");
            }
        }
        return contentType;
    }

    private boolean isPostOrPutMethod(String method) {
        return "Post|Put".toLowerCase().contains(method.toLowerCase());
    }

    private String chooseHttpMethod(StringBuilder builder) {
        String[] commands = new String[] {"Get", "Post", "Put", "Delete"};
        String method = UserInputReader.INSTANCE.chooseCommand("[Step.2] Choose method ", commands);
        builder.append(":method: " + method.toUpperCase()).append("\n");
        return method;
    }

    private void inputUrl(StringBuilder builderRoot) {
        while (true) {
            consolePrinter.print("[Step.3] Enter url : ");

            String input = UserInputReader.INSTANCE.readLine().trim();
            if (StringUtils.isEmpty(input)) {
                throw new UserCancelException();
            }

            try {
                String flowDefinition = parseUriToFlowDefinition(input);
                StringHttpFlowResource.of(flowDefinition).getDocument();
                builderRoot.append(flowDefinition);
                break;

            } catch (MalformedHttpFlowException e) {
                UserInputReader.INSTANCE.printMessageAndWait("ERROR : Malformed url.");

            } catch (IllegalStateException e) {
                if (e.getCause() instanceof URISyntaxException) {
                    UserInputReader.INSTANCE.printMessageAndWait("ERROR : Malformed url.");
                } else {
                    throw e;
                }
            }
        }
    }

    private String parseUriToFlowDefinition(String input) {
        URI uri = HttpFlowUtils.parseURI(input);

        StringBuilder builder = new StringBuilder();
        builder.append(":authority: " + uri.getAuthority()).append("\n");

        String path = uri.getPath();
        if (StringUtils.isEmpty(path)) {
            path = "/";
        }
        builder.append(":path: " + path);

        if (!StringUtils.isEmpty(uri.getQuery())) {
            builder.append("?").append(uri.getQuery());
        }
        builder.append("\n");

        builder.append(":scheme: " + uri.getScheme()).append("\n");
        return builder.toString();
    }

    private void inputHeaders(StringBuilder builder) {
        int number = 1;

        consolePrinter.println("[Step.4] Set request headers");
        while (true) {
            consolePrinter.print("  - [Header #" + number + "] (Enter-Key to skip) : ");

            String input = UserInputReader.INSTANCE.readLineWithMultiLineOption();
            if (StringUtils.isEmpty(input.trim())) {
                break;
            }

            builder.append(input).append("\n");
            number++;
        }
    }

    private String getContentTypeHeader(StringBuilder builder) {
        HttpFlowDocument doc = StringHttpFlowResource.of(builder.toString()).getDocument();
        HttpFlowHeaderValues values = doc.getElements().get(0).getHeader("Content-Type");
        if (values.size() > 0) {
            return values.asList().get(0);
        }
        return null;
    }

    private void inputBody(StringBuilder builder, String method, String contentType) {
        consolePrinter.println("[Step.5] Set request body");
        SelectionDialog dialog = new SelectionDialog("Request body format", "R: Raw text", "M: Multi-line name=value pairs", "S: Skip (Empty body)");
        String format = dialog.open("httpflow> Choose request body format : ");

        if ("S".equalsIgnoreCase(format)) {
            if (isPostOrPutMethod(method)) {
                builder.append("\n");
            } else {
                builder.deleteCharAt(builder.length() - 1);
            }
            return;
        }

        StringBuilder innerBuilder = new StringBuilder();
        if ("R".equalsIgnoreCase(format)) {
            buildRawBodyText(innerBuilder);
        } else {
            buildMultiLineNameValuePairs(innerBuilder);
        }

        if (isPostOrPutMethod(method)) {
            builder.append("\n");
        }

        BodyPostProcessor bodyPostProcessor = BodyPostProcessorFactory.INSTANCE.get(contentType);
        if (bodyPostProcessor != null) {
            PostProcessResult result = bodyPostProcessor.postProcess(method, innerBuilder.toString());
            if (result.getBodyString() != null) {
                builder.append(result.getBodyString());

            } else {
                builder.deleteCharAt(builder.length() - 1);
                builder.deleteCharAt(builder.length() - 1);
            }

        } else {
            builder.append(innerBuilder.toString());
        }
    }

    private void buildRawBodyText(StringBuilder innerBuilder) {
        consolePrinter.println("httpflow> Enter request body text below. (Last line must be 'EOF' command)");
        String input = UserInputReader.INSTANCE.readMultiLine();
        innerBuilder.append(input);
    }

    private void buildMultiLineNameValuePairs(StringBuilder innerBuilder) {
        consolePrinter.println("httpflow> Enter Name=value pair below. (Empty line stop the loop)");
        while (true) {
            String input = UserInputReader.INSTANCE.readLine();
            if (StringUtils.isEmpty(input.trim())) {
                break;
            }

            if (!input.matches(".+=.*")) {
                consolePrinter.println("WARN : Input ignored - invalid name=value format.");
            } else {
                innerBuilder.append(input).append("\n");
            }
        }
        if (innerBuilder.length() > 0) {
            innerBuilder.deleteCharAt(innerBuilder.length() - 1);
        }
    }

}
