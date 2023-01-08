(ns quil-experiments.iterated-maps
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn scale [x y lo hi]
  (let [scaled-x (q/map-range x lo hi 0 (q/width))
        scaled-y (q/map-range y lo hi 0 (q/height))]
    [scaled-x scaled-y]))

(defn parabola [x c]
  (+ (q/sq x) c))

(defn setup []
  (q/frame-rate 20)
  (q/background 255 255 255)
  (q/fill 255 255 255)
  {:point [0 0]
   :next-point [0 0]
   ;; try changing this: 0.25, -0.25, 0.75, -0.75, -1.8
   :c-param (- 1.6378)})

(defn p2p-line [[x1 y1] [x2 y2]]
  (let [[scx1 scy1] (scale x1 y1 -2.0 2.0)
        [scx2 scy2] (scale x2 y2 -2.0 2.0)]
    ;; need to subtract y from height
    ;; because of the origin being top left corner
    (q/line scx1 (- (q/height) scy1)
            scx2 (- (q/height) scy2))))

(defn update-state
  [{:keys [c-param point parabola-point diagonal-point next-point]
    :as state}]
  (let [[x y] next-point
        parabola-v (parabola x c-param)
        prbl-point [x parabola-v]
        diagonal-point [parabola-v parabola-v]]
    (merge state
           {:point next-point
            :parabola-point prbl-point
            :diagonal-point diagonal-point
            :next-point diagonal-point})))

(defn draw-state [{:keys [point parabola-point diagonal-point next-point]}]
  (p2p-line point parabola-point)
  (p2p-line parabola-point diagonal-point))

(q/defsketch iterated-map
  :title "Iterated-map"
  :size [800 800]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
