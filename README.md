# Transaction-Service
To help the kirana store to mentaing their directory


Documentation :

2 Apis Are integrated with required Authorization (Json Web Authentication )

1: @GET(/api/v1/transaction) Query(optional = true) to Get All the Transaction and filter it by date
2: @POST(/api/v1/transaction) 
{
  "type": "CREDIT",
  "currency": "USD",
  "amountInForeignCurrency": "100.00"
}
Sample json With Authorization :"jwt-token"

this api will record transaction in data base .


3: Security we have use json web authentication for now it secret and client is hardcoded in application.yml and use (https://jwt.io/) to create jwt with iat:
sample jwt-token:"eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE3MDUxMzY3MTEsInN1YiI6Im9lcmVhY3QiLCJpYXQiOjE3MDUxMzY1MzF9.C2bcrt-zCShM-OmDx2M_O7bESL-Sya_J7Zdkr_CGX8hjuM5BM6G0ZkYwqGrv-7wmQJ-7WfNKZa3ae3Owsxe5IQ"


DataBase Structure are given in ddl.sql file in code.

