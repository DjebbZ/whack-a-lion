(ns whack-a-lion.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.ui :refer :all]))

(defn spawn-lion [screen]
  (let [lion (texture "lion.png")]
    (assoc lion
     :x (max 0 (- (rand-int (width screen)) (texture! lion :get-region-width)))
     :y (max 0 (- (rand-int (height screen)) (texture! lion :get-region-height))))))

(defn on-lion? [input-x input-y e]
   (and (> input-x (:x e))
        (< input-x (+ (:x e) (texture! e :get-region-width)))
        (> input-y (:y e))
        (< input-y (+ (:y e) (texture! e :get-region-height)))))

(defn touched-lion [{:keys [x y]} entities]
  (-> (filter (partial on-lion? x y)
              entities)
      last))

(defscreen main-screen
  :on-show
  (fn [screen entities]
    (update! screen :renderer (stage))
    (add-timer! screen :spawn-lion 1 1)
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
        entities))))

(defgame whack-a-lion
  :on-create
  (fn [this]
    (set-screen! this main-screen)))

