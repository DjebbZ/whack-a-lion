(ns whack-a-lion.core
  (:require [play-clj.core :refer :all]
            [play-clj.g2d :refer :all]
            [play-clj.ui :refer :all]
            [play-clj.math :refer :all]))

(declare whack-a-lion main-screen score-screen intro-screen)

(defn spawn-lion [screen]
  (let [lion (texture "lion.png")
        lion-w (texture! lion :get-region-width)
        lion-h (texture! lion :get-region-height)]
    (assoc lion
      :x (max 0 (- (rand-int (width screen)) lion-w))
      :y (max 0 (- (rand-int (height screen)) lion-h))
      :width lion-w :height lion-h
      :lion? true
      :death-animation (animation (float 0.1)
                                  (for [i (range 5)]
                                    (assoc
                                        (texture "lion.png")
                                      :angle (* i 72)))
                                  :set-play-mode (play-mode :loop)))))

(defn on-lion? [input-x input-y {:keys [x y width height lion?] :as e}]
  (println e)
  (and lion? (-> (rectangle x y width height)
                 (rectangle! :contains input-x input-y))))

(defn touched-lion [{:keys [x y]} entities]
  (-> (filter (partial on-lion? x y)
              entities)
      last))

(def cat-sound (memoize (fn [] (sound "cat.wav"))))


(defscreen main-screen
           :on-show
           (fn [screen entities]
             (update! screen :renderer (stage) :camera (orthographic))
             (add-timer! screen :spawn-lion 1 4)
             (texture "lion.png")
             #_(spawn-lion screen))

           :on-render
           (fn [screen entities]
             (clear!)
             (render! screen [(assoc (first entities)
                                  :angle (mod (* 300 (:total-time screen)) 360))] #_entities))

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
                 (do
                   (sound! (cat-sound) :play)
                   (run! score-screen :update-score)
                   (replace {touched-lion (merge touched-lion (animation->texture screen (:death-animation touched-lion)))} entities)
                   )
                 entities)))

           :on-resize
           (fn [screen entities]
             (width! screen (* 4 256))
             entities))



(defscreen score-screen
           :on-show
           (fn [screen entities]
             (update! screen :renderer (stage) :camera (orthographic) :score 0)
             (assoc (label "0" (color :white))
               :x 5 :y 5 :label? true))

           :on-render
           (fn [screen entities]
             (let [e (for [entity entities]
                       (case (:label? entity)
                         true (doto entity (label! :set-text (str (:score screen))))
                         entity))]
               (render! screen e)))

           :update-score
           (fn [screen entities]
             (update! screen :score (inc (:score screen)))
             entities)

           :on-resize
           (fn [screen entities]
             (width! screen (* 1 256))))

(defscreen intro-screen
           :on-show
           (fn [screen entities]
             (update! screen :renderer (stage) :camera (orthographic))
             (label "Whack A Lion" (color :white)))

           :on-render
           (fn [screen entities]
             (clear!)
             (render! screen entities))

           :on-resize
           (fn [screen entities]
             (width! screen (* 4 256))
             entities)

           :on-touch-down
           (fn [screen entities]
             (set-screen! whack-a-lion main-screen score-screen)
             entities))

(defgame whack-a-lion
         :on-create
         (fn [this]
           (set-screen! this intro-screen)))

