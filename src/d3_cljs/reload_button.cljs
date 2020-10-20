(ns d3-cljs.reload-button
  (:require [goog.dom :as dom]))

(defn reload-button [reload-fn]
  (let [container (.createElement js/document "div")
        btn (.createElement js/document "button")]
    (some-> (.getElementById js/document "reloadBtnDiv")
      (dom/removeNode))
    (set! (.-id container) "reloadBtnDiv")
    (set! (.-innerText btn) "Reload")
    (set! (.-width (.-style btn)) "75px")
    (set! (.-height (.-style btn)) "50px")
    (.addEventListener btn "click" reload-fn)

    (.appendChild container btn)
    (-> (.getElementById js/document "app")
        (.appendChild container))))
