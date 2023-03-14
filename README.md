# SysCode Task

A simple application to manage students.

## Datas

Every student has 3 attributes:
| Attribute Name | Attribute type | Attribute description | Is it auto-generated? |
|----------------|----------------|-----------------------|-----------------------|
| *id*   | UUID | Data to identify the student | *YES* |
| *fullName*   | String | Full name of the student | *NO* |
| *emailAddress*   | String | E-mail address of the student, form-validated by backend | *NO* |

## API Endpoints

The backend has 4 different endpoint, one for every CRUD method.

### Create
/api/student/addStudent

Request body:
```
{
  "fullName":"Name",
  "emailAddress:"Email address"
}
```

### Read: 
/api/student/getStudents

No request body is required.

### Update: 
/api/student/modifyStudent

Request body:
```
{
  "id":"Student id",
  "fullName":"Name",
  "emailAddress:"Email address"
}
```
*fullName* and *emailAddress* is optional, *id* is required (if id field is empty the response will be an Exception).

Responses:

Status message if the modification was successful:
```
"Save was successful!"
```

### Delete: 
/api/student/deleteStudent

Request body:
```
{
  "id":"Student id"
}
```
*id* is required (if id field is empty the response will be an Exception)
