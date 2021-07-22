(ns swt-multiple-choice.core
  (:require
   [reagent.core :as r]
   [reagent.dom :as d]))

;; -------------------------
;; State

(def statements (r/atom nil))

(def current-statement (r/atom nil))

(def answer-correct? (r/atom nil))

(defn next-statement! []
  (reset! current-statement (rand-nth @statements))
  (reset! answer-correct? nil))

(defn parse-statements [string]
  (->> string
       (re-seq #"([fw]) (.+)")
       (map (fn [[_ correct text]] {:correct (= correct "w") :text text}))))

(defn read-statements [url]
  (-> url
      js/fetch
      (.then #(.text %))
      (.then #(do (reset! statements (parse-statements %)) (next-statement!)))))

(defn answer! [choice]
  (let [current-correct (:correct @current-statement)]
    (swap! answer-correct? (fn [prev] (if (some? prev) prev (= choice current-correct))))))

;; -------------------------
;; Views

(defn button [text color on-click disabled?]
  [:input {:type "button" :value text :style {:color color} :on-click on-click :disabled disabled?}])

(defn buttons []
  (let [answered? (some? @answer-correct?)]
    [:div
     [button "Wahr (w)" (if answered? "grey" "mediumspringgreen") #(answer! true) answered?]
     " "
     [button "Falsch (f)" (if answered? "grey" "mediumorchid") #(answer! false) answered?]
     " "
     [button "Weiter (⏎)" "orange" next-statement! false]]))

(defn statement []
  (let [{:keys [text correct]} @current-statement
        answer @answer-correct?]
    [:div
     [:p text]
   [buttons]
   (when (some? answer)
     [:p
      {:style {:color (if answer "green" "red")}}
      "Die Aussage ist " [:strong (if correct "wahr" "falsch") "."]])]))

;"Deine Antwort war " (if answer "richtig ✅" "falsch ❌")

(defn home-page []
  [:div
   [:h2 "SWT - Wahr oder Falsch?"]
   (if @statements
     [statement]
     [:em "Die Aussagen werden geladen, bitte warten..."])])

;; -------------------------
;; Initialize app

(defn mount-root []
  (read-statements "/static/aussagen.txt")
  (d/render [home-page] (.getElementById js/document "app"))
  (.addEventListener
   js/document
   "keydown"
   (fn [event]
     (case (.-keyCode event)
       (13 32) (next-statement!)
       (49 87 89) (answer! true)
       (50 70 78) (answer! false)
       nil))))

(defn ^:export init! []
  (mount-root))
