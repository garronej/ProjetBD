 CREATE OR REPLACE PROCEDURE DBP.TESTING_SP AS 
    DECLARE
    v_code  NUMBER;
    v_errm  VARCHAR2(64);
    BEGIN
    UPDATE PS_NE_PHONE_TBL SET NE_PHONE_TYPE = 'TEST' WHERE NEMPLID_TBL = 'N14924';

    EXCEPTION
    WHEN OTHERS THEN
    v_code := SQLCODE;
    v_errm := SUBSTR(SQLERRM, 1, 64);
    DBMS_OUTPUT.PUT_LINE (v_code || ' ' || v_errm);
    END TESTING_SP;
    /
