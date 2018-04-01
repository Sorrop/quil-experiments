(ns quill-challenges.sinusoidal-blob
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn setup []
  (q/frame-rate 60)
  {:angle 0.0
   :max-dist (q/dist 0 0 400 400)})

(defn update-state [{:keys [angle max-dist]}]
  {:angle (+ angle 0.1)
   :max-dist max-dist}
  )

(def quarter-PI (/ Math/PI 4))

(defn draw-state [{:keys [angle max-dist]}]
  (q/translate 0 (/ (q/height) 2) 0)
  (q/rotate-x (- (q/atan (q/cos quarter-PI))))
  (q/rotate-z (- quarter-PI))
  (q/ortho -800 800 -600 600 0 1000)
  (q/background 100)
  (q/rect-mode :center)
  (q/fill 12 35 187)
  #_(q/directional-light 255 255 255 0 -1 -1)
  (let [offset (atom 0)]
    (doseq [x (range 0 810 10)
            z (range -160 160 10)
            :let [d         (q/dist x z (/ (q/width) 2) (/ (q/height) 2))
                  ofst     (q/map-range d 0 max-dist (- Math/PI) Math/PI)
                  r-height (-> (q/sin (+ angle ofst))
                               (q/map-range -1 1 0 300))]]
      (q/with-translation [x 0 z]
        (q/box 10 r-height 10))
      (swap! offset #(+ % 0.0003))
      )))


(q/defsketch sinusoidal-blob
  :title "Wavy cube"
  :renderer :p3d
  :size [800 600]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
