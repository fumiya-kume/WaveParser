# WaveParser

WaveParser is fluently library for wave file read/write.

# Feature plan

- [x] Read Wave File Information
- [x] Write to Wave File
- [] validate wave file

# How to Setup

## Step1. Add Mavel Repository in your build.gradle

```groovy
repositories {
    maven {
        url 'http://fumiya-kume.github.io/WaveParser/repository'
    }
}
```

# Step2. Add reference in your app/build.gradle

```groovy
// Wave Parser Library
// Replace specific version
implementation "kuu.nagoya.waveparser:core:0.0.2"
```

# How to use

```kotlin
import kuu.nagoya.waveparser.WaveModel
---
// You can access wave file inforamtion, a.k.a data, samplingRate, number of chnnel
WaveModel.loadFromFile("/path/to/anyone/")

// load from storage
WaveModel.loadFromFile("path/to/anyone)

```
