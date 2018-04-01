(ns quill-experiments.julia-set
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn j-iter
  [sc_x sc_y cx cy max_iter]
  (loop [i 0
         z_x sc_x
         z_y sc_y]
    (if (and (< (+ (q/sq z_x)
                   (q/sq z_y))
                4)
             (< i max_iter))
      (recur (inc i)
             (+ (* 2 z_x z_y) cy)
             (+ (- (q/sq z_x) (q/sq z_y))
                cx))
      i)))

(defn julia
  [x y cx cy max_iter]
  (let [sc_x (q/map-range x 0 (q/width) -2.5 2.5)
        sc_y (q/map-range y 0 (q/height) -2 2)]
    (j-iter sc_x sc_y cx cy max_iter)))

(defn get-color
  [x y color & i]
  (let [hue    (q/map-range color 0 20 0 1.0)
        sat    1.0 #_(q/cos x)
        bright 0.8]
    [hue sat bright]))

(defn setup
  []
  (q/frame-rate 10)
  (q/color-mode :hsb 1.0)
  {:i 0})


(defn update-state
  [{:keys [i]}]
  {:i (inc i)})

(defn draw-state [{:keys [i]}]
  (let [pxls (q/pixels)]
    (doseq [x (range (q/width))
            y (range (q/height))
            :let [index (+ x (* y (q/width)))
                  color (julia x y -0.4 (* -0.6 (q/cos i)) 20)
                  [hue sat bright] (get-color x y color i)]]
      (aset-int pxls index (q/color hue sat bright)))
    (q/update-pixels))
  #_(q/save-frame "fractalz2.jpg"))

(q/defsketch julia-set
  :title "Julia set visuals"
  :renderer :p2d
  :size [1100 750]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
