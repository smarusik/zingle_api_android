## Zingle Android SDK

## Overview

Zingle is a multi-channel communications platform that allows the sending, receiving and automating of conversations between a Business and a Customer.  Zingle is typically interacted with by Businesses via a web browser to manage these conversations with their customers.  The Zingle API provides functionality to developers to act on behalf of either the Business or the Customer.  The Zingle Android SDK provides mobile application developers an easy-to-use layer on top of the Zingle API.

To view the latest API documentation, please refer to: https://github.com/Zingle/rest-api/

### Integrated UI

In addition to the standard API conveniences, the Android SDK also provides an easy to use User Interface to automate the conversation between a Contact and a Service.  The UI can be used on behalf of a Zingle Service.

![](https://github.com/Zingle/android-sdk/blob/master/docs/resources/EmulScreenshot.tiff)

UI Examples
```java
static boolean initializeConnection(String apiURL, String apiVersion, String token, String password);
```
Initializes basic parameters for connecting to API. Doesn't make any verification (it means true answer doesn't mean that login and password are correct and URL is working).

```java
static void addConversation(String serviceId, String contactId, String contactChannelValue);
```
Registers a new conversation in zingle_android_sdk. If succeed, system begins to receive all messages for ZingleContact with specified ID
and it's possible to open UI for this conversation with showConversation().

```java
static void addConversation(String serviceId, String contactId, String contactChannelValue, ConversationAdderBase ca)
```
Same as addConversation(String serviceId, String contactId, String contactChannelValue), but allow to customize the process
through overloading <i>onPreExecute(), onPostExecute(Boolean aBoolean), onProgressUpdate(String... values)</i>.
See <i>ConversationAdder</i> and <i>AsyncTask<Params,Progress,Result></i> for more information.
<br>
onProgressUpdate(String... values) is triggered 4 times:<br>
<bl>
<li>After registering service: values "1" and service's DisplayName (see <i>ZingleService</i>) or "Failed"</li>
<li>After registering contact: values "2" and contact's id (see <i>ZingleContact</i>) of "Failed"</li>
<li>After trying to find proper channel type: values "3" and allowed channel typeclass (see <i>ZingleChannelType</i>) or "Failed"</li>
<li>After registering conversation participants: values "4" and "UI ready".</li>
</bl>


ZNGConversationViewController *chatViewController = [[ZNGConversationViewController alloc] initWithChannelTypeName:@"Contact ID" from:@"123456"];
```


### Zingle Object Model

Model | Description
--- | ---
ZingleSDK | A singleton master object that holds the credentials, stateful information, and the distribution of notifications in the ZingleSDK.
ZNGAccount | [See Zingle Resource Overview - Account](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#account)
ZNGService | [See Zingle Resource Overview - Service](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#service)
ZNGPlan | [See Zingle Resource Overview - Plan](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#plan)
ZNGContact | [See Zingle Resource Overview - Contact](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#contact)
ZNGAvailablePhoneNumber | [See Zingle Resource Overview - Available Phone Number](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#available-phone-number)
ZNGLabel | [See Zingle Resource Overview - Label](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#label)
ZNGCustomField | [See Zingle Resource Overview - Custom Field](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#custom-field)
ZNGCustomFieldOption | [See Zingle Resource Overview Custom Field Option](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#custom-field-option)
ZNGCustomFieldValue | [See Zingle Resource Overview - Custom Field Value](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#custom-field-value)
ZNGChannelType | [See Zingle Resource Overview - Channel Type](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#channel-type)
ZNGServiceChannel | [See Zingle Resource Overview  - Service Channel](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#service-channel)
ZNGContactChannel | [See Zingle Resource Overview - Contact Channel](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#contact-channel)
ZNGMessageCorrespondent | Message Correspondents are the abstract representation of either the Sender or Recipient on a Message.
ZNGMessageAttachment | Message Attachments provide the ability to add binary data, such as images, to messages.
ZNGConversation | Model responsible for maintaining the state of a conversation between a Contact and a Service.
ZNGConversationViewController | UI that manages the conversation between a Contact and a Service.
ZNGAutomation | [See Zingle Resource Overview - Automation](https://github.com/Zingle/rest-api/blob/master/resource_overview.md#automation)

### QuickStarts

#### Synchronous

To view Quick Start using synchronous examples, please see: [ZingleSyncQuickStart.m](Examples/ZingleSyncQuickStart.m)

#### Asynchronous

To view Quick Start using asynchronous examples, please see: [ZingleAsyncQuickStart.m](Examples/ZingleAsyncQuickStart.m)
