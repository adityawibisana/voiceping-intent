


# Voiceping Library

Library for Voiceping, available with Voiceping 3.2.0. In case 3.2.0 is not available yet on [PlayStore](https://play.google.com/store/apps/details?id=com.media2359.voiceping.store), then you can download [this](https://drive.google.com/file/d/14V_cfi2zSVOFrRaxYk_YHK9bpiPpJSDb)

## Getting Started
1. Add the [Jitpack](https://jitpack.io/#adityawibisana/voiceping-intent) repository to your `settings.gradle.kts`: 
```
dependencyResolutionManagement {
		repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
		repositories {
			mavenCentral()
			maven { url 'https://jitpack.io' }
		}
	}
```
2. Add the library
```
dependencies {
	implementation 'com.github.adityawibisana:voiceping-intent:0.0.8'
}
```
3. Initialize on your MainActivity / Application
```css
Voiceping.initialize(context = this)
```
4. That's it!

## Voiceping Action and State
`Action` is an operation that you can perform with Voiceping. `State` is the current Voiceping state, and it's using modern [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow#stateflow). You can listen to to this state to determine whether the action you performed executed correctly.

## Actions
List of possible operations that you can perform with Voiceping. Here are the lists that you can do:

### Start PTT
```css
Voiceping.action.startPTT()
```  
This will start PTT on the current channel. Mic permission must be enabled on Voiceping. See `Voiceping.state.processor`, and check whether the [processor's state](https://github.com/adityawibisana/voiceping-intent#processor) is `StateRecording` to know that this action was successful. Voiceping must be logged in first. See [Login](https://github.com/adityawibisana/voiceping-intent#login).

### Stop PTT
```css
Voiceping.action.stopPTT()
```  
This will stop PTT.  See `Voiceping.state.processor`, and check whether the [processor's state](https://github.com/adityawibisana/voiceping-intent#processor) is `StateIdle` to know that this action was successful.

### Search Channel
```css
Voiceping.action.searchChannel(channelName)
```  
This will search channel and mark that channel as the active channel. You can also search for a user by specifying `channelName` as the user's `displayname`. To check the [channel's state](https://github.com/adityawibisana/voiceping-intent#currentchannel) use `Voiceping.state.currentChannel`.

### Go To Next Channel
```kotlin
Voiceping.action.goToNextChannel()
```  
This will jump to the next channel. The next channel is based on the order seen in Voiceping's Favorite screen. To check the [channel's state](https://github.com/adityawibisana/voiceping-intent#currentchannel) use `Voiceping.state.currentChannel`.

### Go to Previous Channel
```kotlin
Voiceping.action.goToPrevChannel()
```  
This will jump to the previous channel. The previous channel is based on the order seen in Voiceping's Favorite screen. To check the [channel's state](https://github.com/adityawibisana/voiceping-intent#currentchannel) use `Voiceping.state.currentChannel`.

### Login
```kotlin
Voiceping.action.login(username, password)
```  
This allows you to login to Voiceping, using specified `username` and `password`

### Exit
```kotlin
Voiceping.action.logout()
```  
This will stop Voiceping. 

## States
Listen to Voiceping state using [StateFlow](https://developer.android.com/kotlin/flow/stateflow-and-sharedflow#stateflow). Available states:

### User
```kotlin
Voiceping.state.user
```
Currently logged-in user. There are 3 fields:
- `displayname` --> the display name of the user, shown in the app. You normally use this to search for a specific user.
- `username` --> user's username.
- `fullname` --> user's full name.
 
### Processor
```kotlin
Voiceping.state.processor
```  
Current Voiceping processor. The state is either:
#### StatePlaying 
Indicating that Voiceping is currently playing audio, either from live messages or from history.
#### StateRecording
Indicating that Voiceping is currently in a PTT state.
#### StateIdle
Indicating that Voiceping is neither in a playing state nor in a recording state. 
<br/>Sample usage:
```kotlin
// compose:
val processor = Voiceping.state.processor
val currentProcessor = processor.collectAsState().value.name
// if you're using LiveData:
processor.asLiveData().observe(viewLifeCycleOwner) {
// do something	when processor's state is changed
}
```

### Current Channel
```kotlin
Voiceping.state.currentChannel
```  
Currently active channel.  It has 3 properties:
- `name` --> name of the channel
- `type` --> 0 is Group. 1 is Private (1-1 PTT)
<br/>Sample usage:
```kotlin
// compose:
val currentChannel = Voiceping.state.currentChannel
val currentChannelName = currentChannel.collectAsState().value.name
// if you're using LiveData:
currentChannel.asLiveData().observe(viewLifeCycleOwner) {
// do something	when current channel's state is changed
}
```

### Health
```kotlin
Voiceping.state.health
```
Show current Voiceping status. The possible values are:
- `VoicepingReady`: Everything is good. Voiceping is ready.
- `VoicepingIsNotInstalled`: Voiceping is not installed.
- `VoicepingIsNotLoggedIn`: The user is not logged in to Voiceping.
- `VoicepingMicPermissionIsNotGranted`: Mic permission for Voiceping is not granted.
- `VoicepingIsNotConnected`: Voiceping is not connected to Voiceping's server.
- `VoicepingIsNotRunning`: Voiceping is not running. The library will try its best to rerun Voiceping.
<br/>Sample code:
```kotlin
// compose:
val healthState = Voiceping.state.health
val healthStateValue = when (val currentHealthStateFlow = healthState.collectAsState().value) {
            is HealthStatus.VoicepingIsNotConnected -> currentHealthStateFlow.message
            is HealthStatus.VoicepingIsNotInstalled -> currentHealthStateFlow.message
            is HealthStatus.VoicepingIsNotLoggedIn -> currentHealthStateFlow.message
            is HealthStatus.VoicepingMicPermissionIsNotGranted -> currentHealthStateFlow.message
            is HealthStatus.VoicepingServiceIsNotRunning -> currentHealthStateFlow.message
            is HealthStatus.VoicepingIsNotRunning -> currentHealthStateFlow.message
            HealthStatus.VoicepingReady -> {
                healthTextStyle = codeFontStyleOk
                "Ready"
            }
        }
// if you're using LiveData:
healthState.asLiveData().observe(viewLifeCycleOwner) {
// do something when health state is changed
}
```

