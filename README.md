## Zingle Android SDK

## Overview

Zingle is a multi-channel communications platform that allows the sending, receiving and automating of conversations between a Business and a Customer.  Zingle is typically interacted with by Businesses via a web browser to manage these conversations with their customers.  The Zingle API provides functionality to developers to act on behalf of either the Business or the Customer.  The Zingle Android SDK provides mobile application developers an easy-to-use layer on top of the Zingle API.

To view the latest API documentation, please refer to: https://github.com/Zingle/rest-api/

### Integrated UI

In addition to the standard API conveniences, the Android SDK also provides an easy to use User Interface to automate the conversation between a Contact and a Service.  The UI can be used on behalf of a Zingle Service.

![](https://github.com/Zingle/android-sdk/blob/master/zingle_android_sdk/docs/resources/EmulScreenshot.tiff)

###UI Integration

Edit your AndroidManifest.xml, so it contains
```xml
    <uses-permission android:name="android.permission.INTERNET"/>
    <uses-permission android:name="android.permission.VIBRATE"/>
    <uses-feature android:name="android.hardware.camera"
        android:required="false"/>
```
in <i>manifest</i> tag and
```xml
        <activity
            android:name="me.zingle.android_sdk.ZingleMessagingActivity"
            android:label="ZingleMessageList">

        </activity>
        <service
            android:name="me.zingle.android_sdk.daemons.MessageSender"
            android:exported="false">

        </service>
        <service
            android:name="me.zingle.android_sdk.daemons.MessageReceiver"
            android:exported="false">

        </service>
        <service
            android:name="me.zingle.android_sdk.daemons.AttachmentDownloader"
            android:exported="false">

        </service>
```
in <i>application</i> tag.

Then use static methods of ZingleUIInitAndStart class, listed below to initialize connection, add conversations, start services and show UI.

```java
static boolean initializeConnection(String apiURL, String apiVersion, String token, String password);
```
Initializes basic parameters for connecting to API. Doesn't make any verification (it means true answer doesn't mean that login and password are correct and URL is working). Consult Zingle dashboard or support for URL and version strings of current API server. See also [api versioning.](https://github.com/Zingle/rest-api/blob/master/README.md#api-versioning)

After connection initialization you can proceed with registering of conversations. Each conversation supports message exchanging between specified service and contact.

It supposed, that you already have a set of registered services on Zingle server before starting developing. If you want to use predefined subset of them in your application, you can find information about all available services for provided token and password at [dashboard](https://dashboard.zingle.me) (link can be different, so contact Zingle support if in trouble). To obtain this information programmatically use zingle_java_sdk - ZingleServiceServices class methods (see javadocs and [Pure java SDK usage](https://github.com/Zingle/android-sdk/blob/master/README.md#pure-java-sdk-usage) chapter below).

Contacts can be treated the same way as services with only diference, that they also could be added through dashboard or programmatically with zingle_java_sdk - ZingleContactServices, ZingleContactChannelTypeServices and ZingleContactChannelTypeServices class methods. So, you can give application user a predefined contact to make conversation with service, or supply him with some kind of registration functional based on zingle_java_sdk to let him create it himself.

For now only UserDefinedChannel type is supported for exchanging messages through rest API, so your service must support this channel type and contact must have at least one contact channel of this type.
(See [Service Object channel_types](https://github.com/Zingle/rest-api/tree/master/services#service-object) and [Contact Object channels](https://github.com/Zingle/rest-api/tree/master/contacts#contact-object)
for more information.)

With service and contact ids in hand you can proceed to registring conversations.

```java
static void addConversation(String serviceId, String contactId, String contactChannelValue);
```
Registers a new conversation in zingle_android_sdk. If succeed, after triggering startMessageReceiver(), system will receive all messages for ZingleContact with specified ID and it's possible to open UI for this conversation with showConversation(). Can be called as many times as needed to register all conversations.

```java
static void addConversation(String serviceId, String contactId, String contactChannelValue, ConversationAdderBase ca)
```
Same as addConversation(String serviceId, String contactId, String contactChannelValue), but allow to customize the process through overloading <i>onPreExecute(), onPostExecute(Boolean aBoolean), onProgressUpdate(String... values)</i>.
See <i>ConversationAdder</i> and <i>AsyncTask<Params,Progress,Result></i> for more information.
<br>
onProgressUpdate(String... values) is triggered 4 times:<br>
<bl>
<li>After registering service: values "1" and service's DisplayName (see <i>ZingleService</i>) or "Failed"</li>
<li>After registering contact: values "2" and contact's id (see <i>ZingleContact</i>) of "Failed"</li>
<li>After trying to find proper channel type: values "3" and allowed channel typeclass (see <i>ZingleChannelType</i>) or "Failed"</li>
<li>After registering conversation participants: values "4" and "UI ready".</li>
</bl>

```java
static void startMessageReceiver(Context context)
```
Starts message receiving for all registered conversations. Conversations may be added seamlessly before and after triggering this function.

```java
static void showConversation(Context context,String serviceId)
```
Starts and shows the UI for required conversation (defined by <b>serviceId</b>)

Again, all thease functions are static members of ZingleUIInitAndStart class. [Here is a simple example  of using them in android Activity.](https://github.com/Zingle/android-sdk/blob/master/app/src/main/java/me/zingle/zingleapiandroid/StartScreen.java)

For more information refer to javadoc folder (zingle_android_sdk/docs).

### Zingle AndroidSDK Object Model

Model | Description
--- | ---
ZingleUIInitAndStart | Object that holds static methods for quick initialization of Android SDK and starting messaging.

###Pure java SDK usage

If you don't need UI facilities, or want to work with API in some pure java project, the best way is to use Zingle java SDK without integrated Android UI. In this case SDK may be attached to a project as external library. Download [zingle_java_sdk.jar](), add it to project dependencies and initialize SDK with functions listed below.<br>
If you want to change default logging settings use 
```java
static void Log.init(ZingleVerbosityLevel level, PrintStream outputStream)
```
For setting API path and credentials:
```java
static boolean ZingleConnection.init (String apiPath, String apiVersion, String token, String key)
```
After initialization, if your path and credentials are corect, you may use any API functionality by instantiating the proper ZingleXXXXXXServices (XXXXXX stands for object type: Service, Label, Contact etc.) object and using it's methods. For example, this piece of code will list to standard output allowed Zingle services for provided login and password:

```java
import me.zingle.api.sdk.dao.ZingleConnection;
import me.zingle.api.sdk.logger.Log;
import me.zingle.api.sdk.logger.ZingleVerbosityLevel;
import me.zingle.api.sdk.model.*;
import me.zingle.api.sdk.services.*;

public class Main {

    public static void main(String[] args) {

        final String name = "viacheslav.marusyk@cyberhull.com";
        final String password = "123qweasd";

        Log.init(ZingleVerbosityLevel.ZINGLE_VERBOSITY_INFO, System.out);

        if (!ZingleConnection.init("https://qa-api.zingle.me", "v1", name, password)) return;

        ZingleServiceServices serviceServices = new ZingleServiceServices();
        ZingleList<ZingleService> services = serviceServices.list();
        System.out.println(services);
    }
}
```
### Zingle JavaSDK Object Model

Model | Description
--- | ---
ZingleAccount | [See Zingle Resource Overview - Account](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#account)
ZingleAttachment | Message Attachments provide the ability to add binary data, such as images, to messages.
ZingleAutomation | [See Zingle Resource Overview - Automation](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#automation)
ZingleAvailablePhoneNumber | [See Zingle Resource Overview - Available Phone Number](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#available-phone-number)
ZingleChannelType | [See Zingle Resource Overview - Channel Type](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#channel-type)
ZingleContact | [See Zingle Resource Overview - Contact](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#contact)
ZingleContactChannel | [See Zingle Resource Overview - Contact Channel](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#contact-channel)
ZingleContactField | [See Zingle Resource Overview - Custom Field](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#custom-field)
ZingleContactFieldValue | [See Zingle Resource Overview - Custom Field Value](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#custom-field-value)
ZingleCorrespondent | Message Correspondents are the representation of either the Sender or Recipient on a Message.
ZingleFieldOption | [See Zingle Resource Overview Custom Field Option](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#custom-field-option)
ZingleLabel | [See Zingle Resource Overview - Label](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#label)
ZingleMessage | [See Zingle Resource Overview - Message](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#message)
ZinglePlan | [See Zingle Resource Overview - Plan](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#plan)
ZingleService | [See Zingle Resource Overview - Service](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#service)
ZingleServiceChannel | [See Zingle Resource Overview  - Service Channel](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#service-channel)

For more information refer to javadoc folder (zingle_java_sdk/docs).
