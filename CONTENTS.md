# Declaration of parts

## Modules

1. `:app` - Main app module
2. `:lib-pizza-api` - pure java module (well ok, kotlin) declaring the retrofit interface implementing the pizza-api
3. `:pizza:common-ui` - module implementing common non-app specific helpers

## Frameworks/Libraries used

### Common
* Kotlin
    * For that sweet sweet modern programming language feeling.
* RxJava2
    * Because callback chaining is a pain and Reactive Extensions is a painkiller
* Dagger2
    * Not technically required at this scale but might as well since it's pretty much a given for anything bigger than this demo app.

### Android Specific
* Support libs
    * appcompat-v7 - at some point I will write an app not using appcompat. This is not that time.
    * recyclerview-v7 - because we want lists of things. Lists of things means RecyclerView.
    * architecture components - yay, LiveData! yay, LifeCycleOwner! ViewModels!
    * cardview-v7 - until a unique design is decided, cards are usually a good start
    
### Api interaction
* Retrofit2 
    * Personal preference for implementing simple interactions with REST APIs
* Moshi
    * Simple and effective JSON serializing/deserializing

### App specific

* Hannes Dorfmann's Adapter Delegates
    * Simple and pleasant library for heterogeneous RecyclerViews
* ThreeTenABP
    * Android specific backport of JSR-310 (java.time.*). Native time libs are a pain and Joda Time
      is unfortunately just too large for this project
* Thomas Bruyelle's RxPermissions
    * arguably not necessary at this scale but it makes interacting with the new permission system a
    bit nicer
* Google Playservices Location
    * We want locations. FusedLocationProviderClient is the way to go.
* Timber
    * Personal favorite for logging. Easy to configure. Easy to use. Gold.