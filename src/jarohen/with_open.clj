(ns jarohen.with-open
  (:import [java.io Closeable]))

(defn _with-open [resource f]
  (cond
    (instance? Closeable resource) (with-open [_ resource]
                                     (f resource))

    (fn? resource) (resource f)
    :else (throw (ex-info "Invalid resource passed to with-open+" {:resource resource}))))

(defmacro with-open+ [bindings & body]
  (if-let [[binding expr & more-bindings] (seq bindings)]
    `(_with-open ~expr (fn [~binding]
                         (with-open+ [~@more-bindings]
                           ~@body)))

    `(do ~@body)))
