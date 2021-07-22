(ns swt-multiple-choice.comments
  (:require [clojure.string :as str]))

(def comments
  {true
   ["Tichy wäre stolz! Bestimmt!"
    "Du bist der erste, der das richtig hat!!! lol jk"
    "In der Klausur muss das schneller gehen!"
    "Herzlichen Glückwunsch, du bist so schlau wie die anderen 99%."
    "Schön dass du das weißt, aber das kommt safe nicht ran."
    "Ich glaub du fühlst dich gerade etwas zu sicher."
    "Wird das reichen in der Klausur?"
    "Cool. Wirklich toll gemacht. Applaus"]
   false
   ["Ernsthaft? Das war definitiv eine der einfacheren"
    "https://www.sle.kit.edu/downloads/Sonstige/Exmatrikulation_WEB_Formular.pdf"
    "Dass das so nichts wird, wissen wir beide..."
    "Oh je, und das willst du bis zur Klausur noch alles nachholen? hahahahaha"
    "Bist du gewinnend, KIT Student?"
    "..."
    "Ich kann mir sowas nicht mit ansehen..."
    "Willst du vielleicht doch lieber BWL studieren?"]})

(def clippy
  "
                  .,      {message}
            ,╗╩^`\"╙Ñ≥           /
        ▄Φ▀███      ╫H         /
       `   ]Å       ╫▓▓▓▄     /
       ▄▓█▓▄.       ╩   `▀▄  /
     ]╦▀███▀╦╦r  :.╓▓▓▄▄    /
      `╙╩Ñ╫╫╝^   ╬╦║████MN -
          j╫      `╫Å╩╩╩╜
          j╫ ╓     ÑH  ,,
          j╫ Ñ     Ñ⌐  ╫
          ▐╫ Ñ     Ñ  ]H
           ╫ ╫⌐    ╫H ÑH
           ╫⌐╙N    ]H ╠╕
           ╫N ╙N..╥╩  ╚Ñ
            ╫    `    j╫
            ╚N        ]╬
             ╙N      ,Ñ
               *═≡d≡╨\"
")

(defn clippy-says [text]
  (str/replace clippy "{message}" text))

(def comment-chance 0.05)
