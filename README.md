## Introduction

A play-clj game in which ... well, that part is up to you.

## Contents

* `android/src` Android-specific code
* `desktop/resources` Images, audio, and other files
* `desktop/src` Desktop-specific code
* `desktop/src-common` Cross-platform game code
* `ios/src` iOS-specific code

## Building

All projects can be built using [Nightcode](https://nightcode.info/), or on the command line using [Leiningen](https://github.com/technomancy/leiningen) with the [lein-droid](https://github.com/clojure-android/lein-droid) and [lein-fruit](https://github.com/oakes/lein-fruit) plugins.

## REPL : handling exceptions

``` clojure
(play-clj.core/set-screen-wrapper! (fn [screen screen-fn]
                       (try (screen-fn)
                         (catch Exception e
                           (.printStackTrace e)
                           (Thread/sleep 1000)
                           (play-clj.core/set-screen! whack-a-lion.core/whack-a-lion whack-a-lion.core/main-screen whack-a-lion.core/score-screen)))))
```
