package org.example.Model;

public class Call {
    public final Method caller;
    public final Method callee;

    public Call(Method caller, Method callee) {
        this.caller = caller;
        this.callee = callee;
    }

    @Override
    public String toString() {
        return caller + " -> " + callee;
    }
}
