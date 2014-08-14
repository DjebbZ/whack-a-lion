(ns whack-a-lion.core.desktop-launcher
  (:require [whack-a-lion.core :refer :all])
  (:import [com.badlogic.gdx.backends.lwjgl LwjglApplication]
           [org.lwjgl.input Keyboard])
  (:gen-class))

(defn -main
  []
  (LwjglApplication. whack-a-lion "whack-a-lion" 800 600)
  (Keyboard/enableRepeatEvents true))
