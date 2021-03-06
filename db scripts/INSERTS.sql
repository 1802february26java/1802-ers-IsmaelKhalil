/********** Inserts **********/

INSERT INTO USER_ROLE VALUES (1,'EMPLOYEE');
INSERT INTO USER_ROLE VALUES (2,'MANAGER');

INSERT INTO USER_T VALUES (NULL,'PETER','ALAGNA','PALAGNAJR','123456','PETER.ALAGNA@REVATURE.COM',1);

INSERT INTO REIMBURSEMENT_TYPE VALUES (1,'OTHER');
INSERT INTO REIMBURSEMENT_TYPE VALUES (2,'COURSE');
INSERT INTO REIMBURSEMENT_TYPE VALUES (3,'CERTIFICATION');
INSERT INTO REIMBURSEMENT_TYPE VALUES (4,'TRAVELING');

INSERT INTO REIMBURSEMENT_STATUS VALUES (1,'PENDING');
INSERT INTO REIMBURSEMENT_STATUS VALUES (2,'DECLINED');
INSERT INTO REIMBURSEMENT_STATUS VALUES (3,'APPROVED');

INSERT INTO USER_T VALUES (NULL,'TEST','MANAGER','testmanager','123456','JY350200@GMAIL.COM',2);


SELECT * FROM REIMBURSEMENT;
DESCRIBE REIMBURSEMENT;
SELECT * FROM REIMBURSEMENT_TYPE;
SELECT * FROM REIMBURSEMENT_STATUS;

SELECT * FROM USER_T;
INSERT INTO USER_T VALUES (NULL,'TEST','MANAGER','testmanager','123456','JY350200@GMAIL.COM',2);
COMMIT;

INSERT INTO REIMBURSEMENT(R_ID,R_REQUESTED,R_RESOLVED,R_AMOUNT,R_DESCRIPTION,R_RECEIPT,EMPLOYEE_ID,
                          MANAGER_ID,RS_ID,RT_ID)
      VALUES(NULL,TO_DATE('2018-1-1 01:01:01','yyyy-mm-dd hh24:mi:ss'),TO_DATE('2018-1-1 01:01:01','yyyy-mm-dd hh24:mi:ss'),
      100,'FIRST TEST','1111',1,21,1,1);
      COMMIT;
      