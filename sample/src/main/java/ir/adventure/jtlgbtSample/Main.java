package ir.adventure.jtlgbtSample;

import ir.adventure.jtlgbtSample.sampleRunner.WorkflowState;

import java.net.InetSocketAddress;
import java.net.Proxy;

public class Main {
    public static void main(String[] args) {
        Proxy proxyTest = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("127.0.0.1", 8580));
        String token = "";

        //new IAmWatching().runSample(token,proxyTest);
        //new HelloWorld().runSample(token,proxyTest);
        //new Echo().runSample(token,proxyTest);
        //new FullEcho().runSample(token,proxyTest);
        new WorkflowState().runSample(token, proxyTest);
    }
}
