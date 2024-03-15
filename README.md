# atak-compose

## Overview
**atak-compose** is an Android library to ease use of Jetpack Compose within an ATAK plugin

Provides 
- `ComposeContext` to manage the ATAK and plugin `Context`
- an ATAK `ViewModel` to manage Coroutine Scope
- a ComposePane to wrap ATAK's `gov.tak.api.ui.Pane` and set the `ViewCompositionStrategy`
- ATAK navigation idiomatic with Jetpack Navigation

## Sample

For sample usage, refer to Ditto's helloworld-MAD sample ATAK plugin
https://git.tak.gov/ditto-samples/helloworld-mad


## Usage
```
repositories {
    mavenCentral()
}

dependencies {
    implementation libs.com.dittofederal.atakCompose
}

[versions]
com-dittofederal-atak-compose = "0.0.4"

[libraries]
com-dittofederal-atakCompose = { group = "com.dittofederal", name = "atak-compose", version.ref = "com-dittofederal-atak-compose" }
```

This library uses the same `local.properties` configuration used by ATAK plugins e.g. to find the
ATAK SDK at build-time

## Collaboration
PR's will be accepted for bug fixes and feature additions to the extent they are generally useful
to ATAK plugin developers using Compose

DittoLive manages the **atak-compose** library in Maven Central