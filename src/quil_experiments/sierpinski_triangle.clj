(ns quill-experiments.sierpinski-triangle
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(defn get-middle [[x1 y1] [x2 y2]]
  (let [x (/ (+ x1 x2) 2)
        y (/ (+ y1 y2) 2)]
    [x y]))

(defn get-new-point
  [start-points point]
  (let [dice (rand-nth [1 2 3 4 5 6])]
    (cond
      (#{1 2} dice) (get-middle point (get start-points 0))
      (#{3 4} dice) (get-middle point (get start-points 1))
      :else (get-middle point (get start-points 2)))))

(defn setup []
  (q/frame-rate 100)
  (q/background 255)
  {:start-points  [[0 600]
                   [400 0]
                   [800 600]]
   :point [200 200]})

(defn update-state [{:keys [start-points point]}]
  {:start-points start-points
   :point (get-new-point start-points point)})

(defn draw-state [{:keys [point]}]
  (let [[x y] point]
    (q/fill 255 255 255)
    (q/point x y)))

(q/defsketch hello-quill
  :title "You spin my circle right round"
  :size [1000 700]
  ;; setup function called only once, during sketch initialization.
  :setup setup
  ;; update-state is called on each iteration before draw-state.
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
