(ns quil-experiments.visualize-complex
  (:require [quil.core :as q]
            [quil.middleware :as m]))

(def pi java.lang.Math/PI)

(defn c-addition [[a-re a-im] [b-re b-im]]
  [(+ a-re b-re) (+ a-im b-im)])

(defn c-add [[a-re a-im] & more]
  (reduce c-addition [a-re a-im] more))

(defn c-multiply [[a-re a-im] [b-re b-im]]
  (let [c-re (- (* a-re b-re) (* a-im b-im))
        c-im (+ (* a-im b-re) (* a-re b-im))]
    [c-re c-im]))

(defn c-pow [[re im] n]
  (reduce c-multiply [re im] (repeat (dec n) [re im])))

(defn calc-complex [re im t]
  (let [z-pow-5 (c-pow [re im] 5)
        z-pow-4 (c-pow [re im] 4)
        z-pow-3 (c-pow [re im] 3)
        z-pow-2 (c-pow [re (+ im 3)] 2)
        z*n     (c-multiply [re im] [3 0])
        part    (c-add z-pow-5
                       #_(c-multiply z-pow-4 [5 (q/sin (/ t (* 2 pi) 7))])
                       z-pow-3
                       #_(c-multiply z-pow-2
                                     [(q/cos t) 0])
                       z*n)]
    (c-add part [0 0])))

(defn scale [x y lo hi]
  (let [scaled-x (q/map-range x
                              (- (/ (q/width) 2))
                              (/ (q/width) 2)
                              lo
                              hi)
        scaled-y (q/map-range y
                              (- (/ (q/height) 2))
                              (/ (q/height) 2)
                              lo
                              hi)]
    [scaled-x scaled-y]))

(defn setup []
  (q/frame-rate 20)
  (q/color-mode :hsb (* 2 pi))
  {:t 0})

(defn update-state [{:keys [t]}]
  {:t (if (= t 100)
        0
        (inc t))})

(defn draw-state [{:keys [t]}]
  (let [pxls (q/pixels)]
    (doseq [x (range (q/width))
            y (range (q/height))
            :let [index (+ x (* y (q/width)))
                  tx    (- x (/ (q/width) 2))
                  ty    (- y (/ (q/height) 2))
                  [scaled-tx scaled-ty] (scale tx ty -10 10)
                  [mapped-tx mapped-ty] (calc-complex scaled-tx scaled-ty t)
                  hue   (-> (q/atan2 mapped-ty mapped-tx)
                            (q/map-range (- pi) pi 0 (* 2 pi)))]]
      (aset-int pxls index (q/color hue (* 2 pi) (* 2 pi))))
    (q/update-pixels)))


(q/defsketch quil-experiments
  :title "Visualize complex functions"
  :size [700 700]
  :setup setup
  :update update-state
  :draw draw-state
  :features [:keep-on-top]
  :middleware [m/fun-mode])
