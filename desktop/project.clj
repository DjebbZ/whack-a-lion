(defproject whack-a-lion "0.0.1-SNAPSHOT"
  :description "FIXME: write description"
  
  :dependencies [[com.badlogicgames.gdx/gdx "1.3.0"]
                 [com.badlogicgames.gdx/gdx-backend-lwjgl "1.3.0"]
                 [com.badlogicgames.gdx/gdx-box2d "1.3.0"]
                 [com.badlogicgames.gdx/gdx-box2d-platform "1.3.0"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-bullet "1.3.0"]
                 [com.badlogicgames.gdx/gdx-bullet-platform "1.3.0"
                  :classifier "natives-desktop"]
                 [com.badlogicgames.gdx/gdx-platform "1.3.0"
                  :classifier "natives-desktop"]
                 [org.clojure/clojure "1.6.0"]
                 [play-clj "0.3.9"]]
  
  :source-paths ["src" "src-common"]
  :javac-options ["-target" "1.6" "-source" "1.6" "-Xlint:-options"]
  :aot [whack-a-lion.core.desktop-launcher]
  :main whack-a-lion.core.desktop-launcher)
