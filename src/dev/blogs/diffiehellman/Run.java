package dev.blogs.diffiehellman;

public class Run {
    public static void main(String [] args) {
        SideA sideA = new SideA();
        SideB sideB = new SideB();
        Spy spy = new Spy();
         
        sideA.generateSecretKey(sideB, spy);
        
        System.out.println("SideA generates parameters a, p, g and A and sends them to sideB");
        System.out.println("Spy intercepts parameters a, p, g and A from sideA and generates privat parameter c");
        System.out.println("SideB recieves parameters a, p, g and A from sideA and generates privat parameter b");
        
        System.out.println();
        
        System.out.println("SideB generates key based on parameters p, g, b and A");
        
        System.out.println("SideB send public key B to side A");
        System.out.println("Spy intercepts public key B from sideB which oriented for sideA");
        
        System.out.println();
        
        System.out.println("SideA recieves public key B from sideA and generates sercet key based on parameters p, g, a and B");
        System.out.println("Spy generates private key as it exchanged sideB base on parameters p, g, c and A");
        System.out.println("Spy generates private key as it exchanged sideA base on parameters p, g, c and B");
        
        System.out.println();
        
        System.out.println("Have a look at generated private keys of each participents:");
        System.out.print("sideA:                     ");
        sideA.showHashSecretKey();
        System.out.print("sideB:                     ");
        sideB.showHashSecretKey();
        System.out.print("spy as it exchanges sideB: ");
        spy.showHashSecretKey();
        
        System.out.println();
        
        String testMessage = "http://dev-blogs.com";
        System.out.println("SideA enciphers message " + testMessage);
        
        String encipheredMessage = sideA.encipherMessage(testMessage);
        System.out.println();
        System.out.println("Message was enchiphered and now looks like: " + encipheredMessage);
        System.out.println();
        String decipheredMessage = sideB.decipherMessage(encipheredMessage);
        System.out.println("SideB deciphers message: " + decipheredMessage);
        System.out.println();
        
        String decipheredMessageBySpy = spy.decipherMessage(encipheredMessage);
        System.out.println("Spy deciphers message as it exchanges sideB: " + decipheredMessageBySpy);
    }
}
