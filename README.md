HttpFlow란
----------

HttpFlow는 스크립트 형태로 http request를 요청하고 결과를 다른 요청에서 재사용하여<br/>
상호 연관된 2개 이상의 http request를 flow로 정의하여 실행할 수 있는 도구입니다.<br/>
<br/>
아래 예제와 같이 http protocol로 작성된 텍스트를 .hfd 파일로 저장하여 실행할 수 있습니다.<br/>

[ searchCurl.hfd ]
```:authority: www.google.com
:method: GET
:path: /search?q=curl&oq=curl&sourceid=chrome&ie=UTF-8
:scheme: https 
```
(위 형식은 크롬 브라우저의 개발자도구 > Network > Headers > Request Headers에 표시되는 형식으로<br/>
 별도로 편집할 필요 없이 그대로 복사하여 사용할 수 있습니다.)<br/>

OR

```
GET https://www.google.com/search?q=curl&oq=curl&sourceid=chrome&ie=UTF-8 HTTP/1.0
```


설치방법
-------
* 우측의 Releases 에서 httpflow zip파일 또는 tar.gz파일을 다운로드 받습니다.
* 다운로드 받은 파일을 압축해제 해줍니다.
```
unzip httpflow-0.0.1.zip
```
OR
```
tar xzvf httpflow-0.0.1.tar.gz
```
* 압축해제된 디렉토리안에 있는 ``httpflow-0.0.1/bin`` 디렉토리를 ``PATH`` 환경변수에 추가 해줍니다.
* 터미널 커맨드라인에서 ``httpflow -v`` 명령을 실행하여 정상동작 하는 것을 확인해 주세요.
```
bash-3.2$ httpflow.sh -v
Httpflow 0.0.1
```
* 이제 *.hfd 파일로 저장된 http request를 실행할 수 있습니다.
```
bash-3.2$ httpflow.sh searchCurl.hfd
```
