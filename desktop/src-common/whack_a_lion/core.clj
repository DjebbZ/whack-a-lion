(ns whack-a-lion.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.ui :refer :all]
            [play-clj.math :refer :all]))

(defn spawn-lion [screen]
  (let [lion (texture "lion.png")
        lion-w (texture! lion :get-region-width)
        lion-h (texture! lion :get-region-height)]
    (assoc lion
     :x (max 0 (- (rand-int (width screen)) lion-w))
     :y (max 0 (- (rand-int (height screen)) lion-h))
     :width lion-w :height lion-h)))

(defn on-lion? [input-x input-y {:keys [x y width height] :as e}]
  (-> (rectangle x y width height)
      (rectangle! :contains input-x input-y)))

(defn touched-lion [{:keys [x y]} entities]
  (-> (filter (partial on-lion? x y)
              entities)
      last))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage) :camera (orthographic))
    (add-timer! screen :spawn-lion 1 4)
    (spawn-lion screen))

  :on-render
  (fn [screen entities]
    (clear!)
    (render! screen entities))

  :on-timer
  (fn [screen entities]
    (if (= (:id screen) :spawn-lion)
      (conj entities (spawn-lion screen))
      entities))

  :on-touch-down
  (fn [{:keys [input-x input-y] :as screen} entities]
    (let [coords (input->screen screen input-x input-y)
          touched-lion (touched-lion coords entities)]
      (if touched-lion
        (remove #(= touched-lion %) entities)
        entities)))

  :on-resize
  (fn [screen entities]
    (width! screen (* 4 256))
    entities))

(defgame whack-a-lion
  :on-create
  (fn [this]
    (set-screen! this main-screen)))

