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
  (reset! answer-correct? (= choice (:correct @current-statement))))

;; -------------------------
;; Views

(defn button [text color on-click]
  [:input {:type "button" :value text :style {:color color} :on-click on-click}])

(defn buttons []
  [:div
   [button "Wahr" "green" #(answer! true)]
   " "
   [button "Falsch" "red" #(answer! false)]
   " "
   [button "Weiter" "gray" next-statement!]])

(defn statement []
  (let [{:keys [text correct]} @current-statement
        answer @answer-correct?]
    [:div
     [:p
      text
      (when (some? answer)
        [:div
         [:br]
         "Die Aussage ist " [:strong {:style {:color (if correct "green" "red")}} (if correct "wahr" "falsch")] "."])]
   [buttons]
   (when (some? answer)
     [:p
      "Deine Antwort war " (if answer "richtig ✅" "falsch ❌")])]))

(defn home-page []
  [:div
   [:h2 "SWT Multiple Choice Fragen"]
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
       13 (next-statement!)
       (87 89) (answer! true)
       (70 78) (answer! false)
       nil))))

(defn ^:export init! []
  (mount-root))
