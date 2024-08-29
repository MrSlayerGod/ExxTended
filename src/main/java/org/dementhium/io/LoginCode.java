package org.dementhium.io;

public enum LoginCode {
    CreateAccount(5), RemoveId(100), LoginSuccess(2), InvalidPassword(3),
    SuspendedAccount(4), AlreadyLoggedIn(5), WorldIsFull(7), TryLoginAgain(11);


    final public int code;

    LoginCode(int code) {
        this.code = code;
    }
}
