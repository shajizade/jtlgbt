# Java TeLeGram BoT
This is a Wrapper Util on [Pengrad Telegram Bot API](https://github.com/pengrad/java-telegram-bot-api) which make it more a framework than a library.  
## Download

To use this you can add thees snippets to your `build.gradle` :

```groovy
    repositories {
        maven {
            ...
            url  "https://dl.bintray.com/shajizade/maven"
        }
    }
    
    dependencies {
        compile 'ir.adVenture:jtlgbt:v1.0'
    }
```
    
## Usage
To use this framework, first inctanciate `BotManager` like this:
```java
    //Instanciate a new Bot
    bot = new BotManager(botToken);
    //Start Your bot
    bot.start();
```
     
Above snippet is a "Do Nothing Bot" which just get updates.
## I-Am-Watching-Bot
You may have a list of interceptors to be done on any update. Lets add a simple interceptor to have a simple bot which just log every update:
 ```java
    bot.addInterceptor(new Interceptor() {
    
                    //This function will be called on each update
                    @Override
                    public boolean handle(Chain chain) {
                        logger.log("I got an update");
                        
                        //False means do not continue to other interceptors 
                        return false;
                    }
                });
```                
 
 ## Hello-World-Bot
 This interceptor will send a "Hello-World" message in reply of any message:
 ```java
    bot.addInterceptor(new Interceptor() {
                   @Override
                   public boolean handle(Chain chain) {
                       chain.reply("Hello World!");
                       return false;
                   }
               });
```               
               
 ## Echo-Bot
 This interceptor will reply back what you write in your message. It will answer just on "Text Messages":
```java 
    bot.addInterceptor(new Interceptor() {
                   @Override
                   public boolean handle(Chain chain) {
                       if(chain.hasIncomeText()) {
                           chain.reply(chain.incomingText());
                           //No need to continue if it was a text message
                           return false;
                       }
                       //Continue, maybe an appropriate interceptor is waiting for this kind of message 
                       return true;
                   }
               });
```               

## Proxy
There are much more things to be documented here, and I will complete this later. But as Telegam is filtered in many countries this is really important. This snippet shows you how to use this library connecting to a proxy:
```java
    Proxy proxy = new java.net.Proxy(Proxy.Type.HTTP, new InetSocketAddress(ip, port));
    bot = new BotManager(botToken, proxy);
```               

_You can find me here: s.hajizadeh@gmail.com_