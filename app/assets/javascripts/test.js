! function() {
    "use strict";
    var e = "undefined" != typeof globalThis ? globalThis : "undefined" != typeof window ? window : "undefined" != typeof global ? global : "undefined" != typeof self ? self : {};

    function t(e, t) {
        return e(t = {
            exports: {}
        }, t.exports), t.exports
    }
    var n = t((function(t, n) {
        ! function(t) {
            function n(e, t) {
                if (window.NodeList.prototype.forEach) return e.forEach(t);
                for (var n = 0; n < e.length; n++) t.call(window, e[n], n, e)
            }

            function o(e) {
                this.$module = e, this.moduleId = e.getAttribute("id"), this.$sections = e.querySelectorAll(".govuk-accordion__section"), this.$openAllButton = "", this.browserSupportsSessionStorage = i.checkForSessionStorage(), this.controlsClass = "govuk-accordion__controls", this.openAllClass = "govuk-accordion__open-all", this.iconClass = "govuk-accordion__icon", this.sectionHeaderClass = "govuk-accordion__section-header", this.sectionHeaderFocusedClass = "govuk-accordion__section-header--focused", this.sectionHeadingClass = "govuk-accordion__section-heading", this.sectionSummaryClass = "govuk-accordion__section-summary", this.sectionButtonClass = "govuk-accordion__section-button", this.sectionExpandedClass = "govuk-accordion__section--expanded"
            }(function(e) {
                var t, n, o;
                "defineProperty" in Object && function() {
                    try {
                        return Object.defineProperty({}, "test", {
                            value: 42
                        }), !0
                    } catch (e) {
                        return !1
                    }
                }() || (t = Object.defineProperty, n = Object.prototype.hasOwnProperty("__defineGetter__"), o = "A property cannot both have accessors and be writable or have a value", Object.defineProperty = function(e, i, r) {
                    if (t && (e === window || e === document || e === Element.prototype || e instanceof Element)) return t(e, i, r);
                    if (null === e || !(e instanceof Object || "object" == typeof e)) throw new TypeError("Object.defineProperty called on non-object");
                    if (!(r instanceof Object)) throw new TypeError("Property description must be an object");
                    var s = String(i),
                        a = "value" in r || "writable" in r,
                        c = "get" in r && typeof r.get,
                        u = "set" in r && typeof r.set;
                    if (c) {
                        if ("function" !== c) throw new TypeError("Getter must be a function");
                        if (!n) throw new TypeError("Getters & setters cannot be defined on this javascript engine");
                        if (a) throw new TypeError(o);
                        Object.__defineGetter__.call(e, s, r.get)
                    } else e[s] = r.value;
                    if (u) {
                        if ("function" !== u) throw new TypeError("Setter must be a function");
                        if (!n) throw new TypeError("Getters & setters cannot be defined on this javascript engine");
                        if (a) throw new TypeError(o);
                        Object.__defineSetter__.call(e, s, r.set)
                    }
                    return "value" in r && (e[s] = r.value), e
                })
            }).call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
                function(e) {
                    "bind" in Function.prototype || Object.defineProperty(Function.prototype, "bind", {
                        value: function(e) {
                            var t, n = Array,
                                o = Object,
                                i = o.prototype,
                                r = n.prototype,
                                s = function() {},
                                a = i.toString,
                                c = "function" == typeof Symbol && "symbol" == typeof Symbol.toStringTag,
                                u = Function.prototype.toString,
                                l = function(e) {
                                    try {
                                        return u.call(e), !0
                                    } catch (t) {
                                        return !1
                                    }
                                },
                                d = "[object Function]",
                                h = "[object GeneratorFunction]";
                            t = function(e) {
                                if ("function" != typeof e) return !1;
                                if (c) return l(e);
                                var t = a.call(e);
                                return t === d || t === h
                            };
                            var p = r.slice,
                                f = r.concat,
                                m = r.push,
                                b = Math.max,
                                v = this;
                            if (!t(v)) throw new TypeError("Function.prototype.bind called on incompatible " + v);
                            for (var y, w = p.call(arguments, 1), g = function() {
                                    if (this instanceof y) {
                                        var t = v.apply(this, f.call(w, p.call(arguments)));
                                        return o(t) === t ? t : this
                                    }
                                    return v.apply(e, f.call(w, p.call(arguments)))
                                }, E = b(0, v.length - w.length), L = [], k = 0; k < E; k++) m.call(L, "$" + k);
                            return y = Function("binder", "return function (" + L.join(",") + "){ return binder.apply(this, arguments); }")(g), v.prototype && (s.prototype = v.prototype, y.prototype = new s, s.prototype = null), y
                        }
                    })
                }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
                function(e) {
                    var t;
                    "DOMTokenList" in this && (!("classList" in (t = document.createElement("x"))) || !t.classList.toggle("x", !1) && !t.className) || function(t) {
                        var n;
                        "DOMTokenList" in t && t.DOMTokenList && (!document.createElementNS || !document.createElementNS("http://www.w3.org/2000/svg", "svg") || document.createElementNS("http://www.w3.org/2000/svg", "svg").classList instanceof DOMTokenList) || (t.DOMTokenList = function() {
                                var t = !0,
                                    o = function(e, n, o, i) {
                                        Object.defineProperty ? Object.defineProperty(e, n, {
                                            configurable: !1 === t || !!i,
                                            get: o
                                        }) : e.__defineGetter__(n, o)
                                    };
                                try {
                                    o({}, "support")
                                } catch (n) {
                                    t = !1
                                }
                                return function(t, n) {
                                    var i = this,
                                        r = [],
                                        s = {},
                                        a = 0,
                                        c = 0,
                                        u = function(e) {
                                            o(i, e, (function() {
                                                return d(), r[e]
                                            }), !1)
                                        },
                                        l = function() {
                                            if (a >= c)
                                                for (; c < a; ++c) u(c)
                                        },
                                        d = function() {
                                            var e, o, i = arguments,
                                                c = /\s+/;
                                            if (i.length)
                                                for (o = 0; o < i.length; ++o)
                                                    if (c.test(i[o])) throw (e = new SyntaxError('String "' + i[o] + '" contains an invalid character')).code = 5, e.name = "InvalidCharacterError", e;
                                            for ("" === (r = "object" == typeof t[n] ? ("" + t[n].baseVal).replace(/^\s+|\s+$/g, "").split(c) : ("" + t[n]).replace(/^\s+|\s+$/g, "").split(c))[0] && (r = []), s = {}, o = 0; o < r.length; ++o) s[r[o]] = !0;
                                            a = r.length, l()
                                        };
                                    return d(), o(i, "length", (function() {
                                        return d(), a
                                    })), i.toLocaleString = i.toString = function() {
                                        return d(), r.join(" ")
                                    }, i.item = function(e) {
                                        return d(), r[e]
                                    }, i.contains = function(e) {
                                        return d(), !!s[e]
                                    }, i.add = function() {
                                        d.apply(i, e = arguments);
                                        for (var e, o, c = 0, u = e.length; c < u; ++c) o = e[c], s[o] || (r.push(o), s[o] = !0);
                                        a !== r.length && (a = r.length >>> 0, "object" == typeof t[n] ? t[n].baseVal = r.join(" ") : t[n] = r.join(" "), l())
                                    }, i.remove = function() {
                                        d.apply(i, e = arguments);
                                        for (var e, o = {}, c = 0, u = []; c < e.length; ++c) o[e[c]] = !0, delete s[e[c]];
                                        for (c = 0; c < r.length; ++c) o[r[c]] || u.push(r[c]);
                                        r = u, a = u.length >>> 0, "object" == typeof t[n] ? t[n].baseVal = r.join(" ") : t[n] = r.join(" "), l()
                                    }, i.toggle = function(t, n) {
                                        return d.apply(i, [t]), e !== n ? n ? (i.add(t), !0) : (i.remove(t), !1) : s[t] ? (i.remove(t), !1) : (i.add(t), !0)
                                    }, i
                                }
                            }()), "classList" in (n = document.createElement("span")) && (n.classList.toggle("x", !1), n.classList.contains("x") && (n.classList.constructor.prototype.toggle = function(t) {
                                var n = arguments[1];
                                if (n === e) {
                                    var o = !this.contains(t);
                                    return this[o ? "add" : "remove"](t), o
                                }
                                return this[(n = !!n) ? "add" : "remove"](t), n
                            })),
                            function() {
                                var e = document.createElement("span");
                                if ("classList" in e && (e.classList.add("a", "b"), !e.classList.contains("b"))) {
                                    var t = e.classList.constructor.prototype.add;
                                    e.classList.constructor.prototype.add = function() {
                                        for (var e = arguments, n = arguments.length, o = 0; o < n; o++) t.call(this, e[o])
                                    }
                                }
                            }(),
                            function() {
                                var e = document.createElement("span");
                                if ("classList" in e && (e.classList.add("a"), e.classList.add("b"), e.classList.remove("a", "b"), e.classList.contains("b"))) {
                                    var t = e.classList.constructor.prototype.remove;
                                    e.classList.constructor.prototype.remove = function() {
                                        for (var e = arguments, n = arguments.length, o = 0; o < n; o++) t.call(this, e[o])
                                    }
                                }
                            }()
                    }(this)
                }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
                function(e) {
                    "Document" in this || "undefined" == typeof WorkerGlobalScope && "function" != typeof importScripts && (this.HTMLDocument ? this.Document = this.HTMLDocument : (this.Document = this.HTMLDocument = document.constructor = new Function("return function Document() {}")(), this.Document.prototype = document))
                }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
                function(e) {
                    "Element" in this && "HTMLElement" in this || function() {
                        if (!window.Element || window.HTMLElement) {
                            window.Element = window.HTMLElement = new Function("return function Element() {}")();
                            var e, t = document.appendChild(document.createElement("body")),
                                n = t.appendChild(document.createElement("iframe")).contentWindow.document,
                                o = Element.prototype = n.appendChild(n.createElement("*")),
                                i = {},
                                r = function(e, t) {
                                    var n, o, s, a = e.childNodes || [],
                                        c = -1;
                                    if (1 === e.nodeType && e.constructor !== Element)
                                        for (n in e.constructor = Element, i) o = i[n], e[n] = o;
                                    for (; s = t && a[++c];) r(s, t);
                                    return e
                                },
                                s = document.getElementsByTagName("*"),
                                a = document.createElement,
                                c = 100;
                            o.attachEvent("onpropertychange", (function(e) {
                                for (var t, n = e.propertyName, r = !i.hasOwnProperty(n), a = o[n], c = i[n], u = -1; t = s[++u];) 1 === t.nodeType && (r || t[n] === c) && (t[n] = a);
                                i[n] = a
                            })), o.constructor = Element, o.hasAttribute || (o.hasAttribute = function(e) {
                                return null !== this.getAttribute(e)
                            }), u() || (document.onreadystatechange = u, e = setInterval(u, 25)), document.createElement = function(e) {
                                var t = a(String(e).toLowerCase());
                                return r(t)
                            }, document.removeChild(t)
                        } else window.HTMLElement = window.Element;

                        function u() {
                            return c-- || clearTimeout(e), !(!document.body || document.body.prototype || !/(complete|interactive)/.test(document.readyState) || (r(document, !0), e && document.body.prototype && clearTimeout(e), !document.body.prototype))
                        }
                    }()
                }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
                function(e) {
                    var t;
                    "document" in this && "classList" in document.documentElement && "Element" in this && "classList" in Element.prototype && ((t = document.createElement("span")).classList.add("a", "b"), t.classList.contains("b")) || function(e) {
                        var n = !0,
                            o = function(e, t, o, i) {
                                Object.defineProperty ? Object.defineProperty(e, t, {
                                    configurable: !1 === n || !!i,
                                    get: o
                                }) : e.__defineGetter__(t, o)
                            };
                        try {
                            o({}, "support")
                        } catch (t) {
                            n = !1
                        }
                        var i = function(e, t, r) {
                            o(e.prototype, t, (function() {
                                var e, s = "__defineGetter__DEFINE_PROPERTY" + t;
                                if (this[s]) return e;
                                if (this[s] = !0, !1 === n) {
                                    for (var a, c = i.mirror || document.createElement("div"), u = c.childNodes, l = u.length, d = 0; d < l; ++d)
                                        if (u[d]._R === this) {
                                            a = u[d];
                                            break
                                        } a || (a = c.appendChild(document.createElement("div"))), e = DOMTokenList.call(a, this, r)
                                } else e = new DOMTokenList(this, r);
                                return o(this, t, (function() {
                                    return e
                                })), delete this[s], e
                            }), !0)
                        };
                        i(e.Element, "classList", "className"), i(e.HTMLElement, "classList", "className"), i(e.HTMLLinkElement, "relList", "rel"), i(e.HTMLAnchorElement, "relList", "rel"), i(e.HTMLAreaElement, "relList", "rel")
                    }(this)
                }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}), o.prototype.init = function() {
                    if (this.$module) {
                        this.initControls(), this.initSectionHeaders();
                        var e = this.checkIfAllSectionsOpen();
                        this.updateOpenAllButton(e)
                    }
                }, o.prototype.initControls = function() {
                    this.$openAllButton = document.createElement("button"), this.$openAllButton.setAttribute("type", "button"), this.$openAllButton.innerHTML = 'Open all <span class="govuk-visually-hidden">sections</span>', this.$openAllButton.setAttribute("class", this.openAllClass), this.$openAllButton.setAttribute("aria-expanded", "false"), this.$openAllButton.setAttribute("type", "button");
                    var e = document.createElement("div");
                    e.setAttribute("class", this.controlsClass), e.appendChild(this.$openAllButton), this.$module.insertBefore(e, this.$module.firstChild), this.$openAllButton.addEventListener("click", this.onOpenOrCloseAllToggle.bind(this))
                }, o.prototype.initSectionHeaders = function() {
                    n(this.$sections, function(e, t) {
                        var n = e.querySelector("." + this.sectionHeaderClass);
                        this.initHeaderAttributes(n, t), this.setExpanded(this.isExpanded(e), e), n.addEventListener("click", this.onSectionToggle.bind(this, e)), this.setInitialState(e)
                    }.bind(this))
                }, o.prototype.initHeaderAttributes = function(e, t) {
                    var n = this,
                        o = e.querySelector("." + this.sectionButtonClass),
                        i = e.querySelector("." + this.sectionHeadingClass),
                        r = e.querySelector("." + this.sectionSummaryClass),
                        s = document.createElement("button");
                    s.setAttribute("type", "button"), s.setAttribute("id", this.moduleId + "-heading-" + (t + 1)), s.setAttribute("aria-controls", this.moduleId + "-content-" + (t + 1));
                    for (var a = 0; a < o.attributes.length; a++) {
                        var c = o.attributes.item(a);
                        s.setAttribute(c.nodeName, c.nodeValue)
                    }
                    s.addEventListener("focusin", (function(t) {
                        e.classList.contains(n.sectionHeaderFocusedClass) || (e.className += " " + n.sectionHeaderFocusedClass)
                    })), s.addEventListener("blur", (function(t) {
                        e.classList.remove(n.sectionHeaderFocusedClass)
                    })), null != r && s.setAttribute("aria-describedby", this.moduleId + "-summary-" + (t + 1)), s.innerHTML = o.innerHTML, i.removeChild(o), i.appendChild(s);
                    var u = document.createElement("span");
                    u.className = this.iconClass, u.setAttribute("aria-hidden", "true"), s.appendChild(u)
                }, o.prototype.onSectionToggle = function(e) {
                    var t = this.isExpanded(e);
                    this.setExpanded(!t, e), this.storeState(e)
                }, o.prototype.onOpenOrCloseAllToggle = function() {
                    var e = this,
                        t = this.$sections,
                        o = !this.checkIfAllSectionsOpen();
                    n(t, (function(t) {
                        e.setExpanded(o, t), e.storeState(t)
                    })), e.updateOpenAllButton(o)
                }, o.prototype.setExpanded = function(e, t) {
                    t.querySelector("." + this.sectionButtonClass).setAttribute("aria-expanded", e), e ? t.classList.add(this.sectionExpandedClass) : t.classList.remove(this.sectionExpandedClass);
                    var n = this.checkIfAllSectionsOpen();
                    this.updateOpenAllButton(n)
                }, o.prototype.isExpanded = function(e) {
                    return e.classList.contains(this.sectionExpandedClass)
                }, o.prototype.checkIfAllSectionsOpen = function() {
                    return this.$sections.length === this.$module.querySelectorAll("." + this.sectionExpandedClass).length
                }, o.prototype.updateOpenAllButton = function(e) {
                    var t = e ? "Close all" : "Open all";
                    t += '<span class="govuk-visually-hidden"> sections</span>', this.$openAllButton.setAttribute("aria-expanded", e), this.$openAllButton.innerHTML = t
                };
            var i = {
                checkForSessionStorage: function() {
                    var e, t = "this is the test string";
                    try {
                        return window.sessionStorage.setItem(t, t), e = window.sessionStorage.getItem(t) === t.toString(), window.sessionStorage.removeItem(t), e
                    } catch (n) {
                        "undefined" != typeof console && "undefined" != typeof console.log || console.log("Notice: sessionStorage not available.")
                    }
                }
            };

            function r(e) {
                this.$module = e, this.debounceFormSubmitTimer = null
            }

            function s(e) {
                this.$module = e
            }

            function a(e) {
                this.$module = e, this.$textarea = e.querySelector(".govuk-js-character-count"), this.$textarea && (this.$countMessage = e.querySelector('[id="' + this.$textarea.id + '-info"]'))
            }

            function c(e) {
                this.$module = e, this.$inputs = e.querySelectorAll('input[type="checkbox"]')
            }

            function u(e) {
                this.$module = e
            }

            function l(e) {
                this.$module = e
            }

            function d(e) {
                this.$module = e, this.$menuButton = e && e.querySelector(".govuk-js-header-toggle"), this.$menu = this.$menuButton && e.querySelector("#" + this.$menuButton.getAttribute("aria-controls"))
            }

            function h(e) {
                this.$module = e, this.$inputs = e.querySelectorAll('input[type="radio"]')
            }

            function p(e) {
                this.$module = e, this.$tabs = e.querySelectorAll(".govuk-tabs__tab"), this.keys = {
                    left: 37,
                    right: 39,
                    up: 38,
                    down: 40
                }, this.jsHiddenClass = "govuk-tabs__panel--hidden"
            }
            o.prototype.storeState = function(e) {
                    if (this.browserSupportsSessionStorage) {
                        var t = e.querySelector("." + this.sectionButtonClass);
                        if (t) {
                            var n = t.getAttribute("aria-controls"),
                                o = t.getAttribute("aria-expanded");
                            void 0 !== n || "undefined" != typeof console && "undefined" != typeof console.log || console.error(new Error("No aria controls present in accordion section heading.")), void 0 !== o || "undefined" != typeof console && "undefined" != typeof console.log || console.error(new Error("No aria expanded present in accordion section heading.")), n && o && window.sessionStorage.setItem(n, o)
                        }
                    }
                }, o.prototype.setInitialState = function(e) {
                    if (this.browserSupportsSessionStorage) {
                        var t = e.querySelector("." + this.sectionButtonClass);
                        if (t) {
                            var n = t.getAttribute("aria-controls"),
                                o = n ? window.sessionStorage.getItem(n) : null;
                            null !== o && this.setExpanded("true" === o, e)
                        }
                    }
                },
                function(e) {
                    "Window" in this || "undefined" == typeof WorkerGlobalScope && "function" != typeof importScripts && function(e) {
                        e.constructor ? e.Window = e.constructor : (e.Window = e.constructor = new Function("return function Window() {}")()).prototype = this
                    }(this)
                }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
                function(e) {
                    (function(e) {
                        if (!("Event" in e)) return !1;
                        if ("function" == typeof e.Event) return !0;
                        try {
                            return new Event("click"), !0
                        } catch (t) {
                            return !1
                        }
                    })(this) || function() {
                        var t = {
                            click: 1,
                            dblclick: 1,
                            keyup: 1,
                            keypress: 1,
                            keydown: 1,
                            mousedown: 1,
                            mouseup: 1,
                            mousemove: 1,
                            mouseover: 1,
                            mouseenter: 1,
                            mouseleave: 1,
                            mouseout: 1,
                            storage: 1,
                            storagecommit: 1,
                            textinput: 1
                        };
                        if ("undefined" != typeof document && "undefined" != typeof window) {
                            var n = window.Event && window.Event.prototype || null;
                            window.Event = Window.prototype.Event = function(t, n) {
                                if (!t) throw new Error("Not enough arguments");
                                var o;
                                if ("createEvent" in document) {
                                    o = document.createEvent("Event");
                                    var i = !(!n || n.bubbles === e) && n.bubbles,
                                        r = !(!n || n.cancelable === e) && n.cancelable;
                                    return o.initEvent(t, i, r), o
                                }
                                return (o = document.createEventObject()).type = t, o.bubbles = !(!n || n.bubbles === e) && n.bubbles, o.cancelable = !(!n || n.cancelable === e) && n.cancelable, o
                            }, n && Object.defineProperty(window.Event, "prototype", {
                                configurable: !1,
                                enumerable: !1,
                                writable: !0,
                                value: n
                            }), "createEvent" in document || (window.addEventListener = Window.prototype.addEventListener = Document.prototype.addEventListener = Element.prototype.addEventListener = function() {
                                var e = this,
                                    n = arguments[0],
                                    i = arguments[1];
                                if (e === window && n in t) throw new Error("In IE8 the event: " + n + " is not available on the window object. Please see https://github.com/Financial-Times/polyfill-service/issues/317 for more information.");
                                e._events || (e._events = {}), e._events[n] || (e._events[n] = function(t) {
                                    var n, i = e._events[t.type].list,
                                        r = i.slice(),
                                        s = -1,
                                        a = r.length;
                                    for (t.preventDefault = function() {
                                            !1 !== t.cancelable && (t.returnValue = !1)
                                        }, t.stopPropagation = function() {
                                            t.cancelBubble = !0
                                        }, t.stopImmediatePropagation = function() {
                                            t.cancelBubble = !0, t.cancelImmediate = !0
                                        }, t.currentTarget = e, t.relatedTarget = t.fromElement || null, t.target = t.target || t.srcElement || e, t.timeStamp = (new Date).getTime(), t.clientX && (t.pageX = t.clientX + document.documentElement.scrollLeft, t.pageY = t.clientY + document.documentElement.scrollTop); ++s < a && !t.cancelImmediate;) s in r && -1 !== o(i, n = r[s]) && "function" == typeof n && n.call(e, t)
                                }, e._events[n].list = [], e.attachEvent && e.attachEvent("on" + n, e._events[n])), e._events[n].list.push(i)
                            }, window.removeEventListener = Window.prototype.removeEventListener = Document.prototype.removeEventListener = Element.prototype.removeEventListener = function() {
                                var e, t = this,
                                    n = arguments[0],
                                    i = arguments[1];
                                t._events && t._events[n] && t._events[n].list && -1 !== (e = o(t._events[n].list, i)) && (t._events[n].list.splice(e, 1), t._events[n].list.length || (t.detachEvent && t.detachEvent("on" + n, t._events[n]), delete t._events[n]))
                            }, window.dispatchEvent = Window.prototype.dispatchEvent = Document.prototype.dispatchEvent = Element.prototype.dispatchEvent = function(e) {
                                if (!arguments.length) throw new Error("Not enough arguments");
                                if (!e || "string" != typeof e.type) throw new Error("DOM Events Exception 0");
                                var t = this,
                                    n = e.type;
                                try {
                                    if (!e.bubbles) {
                                        e.cancelBubble = !0;
                                        var o = function(e) {
                                            e.cancelBubble = !0, (t || window).detachEvent("on" + n, o)
                                        };
                                        this.attachEvent("on" + n, o)
                                    }
                                    this.fireEvent("on" + n, e)
                                } catch (i) {
                                    e.target = t;
                                    do {
                                        e.currentTarget = t, "_events" in t && "function" == typeof t._events[n] && t._events[n].call(t, e), "function" == typeof t["on" + n] && t["on" + n].call(t, e), t = 9 === t.nodeType ? t.parentWindow : t.parentNode
                                    } while (t && !e.cancelBubble)
                                }
                                return !0
                            }, document.attachEvent("onreadystatechange", (function() {
                                "complete" === document.readyState && document.dispatchEvent(new Event("DOMContentLoaded", {
                                    bubbles: !0
                                }))
                            })))
                        }

                        function o(e, t) {
                            for (var n = -1, o = e.length; ++n < o;)
                                if (n in e && e[n] === t) return n;
                            return -1
                        }
                    }()
                }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}), r.prototype.handleKeyDown = function(e) {
                    var t = e.target;
                    "button" === t.getAttribute("role") && 32 === e.keyCode && (e.preventDefault(), t.click())
                }, r.prototype.debounce = function(e) {
                    if ("true" === e.target.getAttribute("data-prevent-double-click")) return this.debounceFormSubmitTimer ? (e.preventDefault(), !1) : void(this.debounceFormSubmitTimer = setTimeout(function() {
                        this.debounceFormSubmitTimer = null
                    }.bind(this), 1e3))
                }, r.prototype.init = function() {
                    this.$module.addEventListener("keydown", this.handleKeyDown), this.$module.addEventListener("click", this.debounce)
                }, s.prototype.init = function() {
                    this.$module && ("boolean" == typeof this.$module.open || this.polyfillDetails())
                }, s.prototype.polyfillDetails = function() {
                    var e, t = this.$module,
                        n = this.$summary = t.getElementsByTagName("summary").item(0),
                        o = this.$content = t.getElementsByTagName("div").item(0);
                    n && o && (o.id || (o.id = "details-content-" + (e = (new Date).getTime(), "undefined" != typeof window.performance && "function" == typeof window.performance.now && (e += window.performance.now()), "xxxxxxxx-xxxx-4xxx-yxxx-xxxxxxxxxxxx".replace(/[xy]/g, (function(t) {
                        var n = (e + 16 * Math.random()) % 16 | 0;
                        return e = Math.floor(e / 16), ("x" === t ? n : 3 & n | 8).toString(16)
                    })))), t.setAttribute("role", "group"), n.setAttribute("role", "button"), n.setAttribute("aria-controls", o.id), n.tabIndex = 0, !0 == (null !== t.getAttribute("open")) ? (n.setAttribute("aria-expanded", "true"), o.setAttribute("aria-hidden", "false")) : (n.setAttribute("aria-expanded", "false"), o.setAttribute("aria-hidden", "true"), o.style.display = "none"), this.polyfillHandleInputs(n, this.polyfillSetAttributes.bind(this)))
                }, s.prototype.polyfillSetAttributes = function() {
                    var e = this.$module,
                        t = this.$summary,
                        n = this.$content,
                        o = "true" === t.getAttribute("aria-expanded"),
                        i = "true" === n.getAttribute("aria-hidden");
                    return t.setAttribute("aria-expanded", o ? "false" : "true"), n.setAttribute("aria-hidden", i ? "false" : "true"), n.style.display = o ? "none" : "", null !== e.getAttribute("open") ? e.removeAttribute("open") : e.setAttribute("open", "open"), !0
                }, s.prototype.polyfillHandleInputs = function(e, t) {
                    e.addEventListener("keypress", (function(e) {
                        var n = e.target;
                        13 !== e.keyCode && 32 !== e.keyCode || "summary" === n.nodeName.toLowerCase() && (e.preventDefault(), n.click ? n.click() : t(e))
                    })), e.addEventListener("keyup", (function(e) {
                        var t = e.target;
                        32 === e.keyCode && "summary" === t.nodeName.toLowerCase() && e.preventDefault()
                    })), e.addEventListener("click", t)
                }, a.prototype.defaults = {
                    characterCountAttribute: "data-maxlength",
                    wordCountAttribute: "data-maxwords"
                }, a.prototype.init = function() {
                    var e = this.$module,
                        t = this.$textarea,
                        n = this.$countMessage;
                    if (t && n) {
                        t.insertAdjacentElement("afterend", n), this.options = this.getDataset(e);
                        var o = this.defaults.characterCountAttribute;
                        this.options.maxwords && (o = this.defaults.wordCountAttribute), this.maxLength = e.getAttribute(o), this.maxLength && (e.removeAttribute("maxlength"), "onpageshow" in window ? window.addEventListener("pageshow", this.sync.bind(this)) : window.addEventListener("DOMContentLoaded", this.sync.bind(this)), this.sync())
                    }
                }, a.prototype.sync = function() {
                    this.bindChangeEvents(), this.updateCountMessage()
                }, a.prototype.getDataset = function(e) {
                    var t = {},
                        n = e.attributes;
                    if (n)
                        for (var o = 0; o < n.length; o++) {
                            var i = n[o],
                                r = i.name.match(/^data-(.+)/);
                            r && (t[r[1]] = i.value)
                        }
                    return t
                }, a.prototype.count = function(e) {
                    return this.options.maxwords ? (e.match(/\S+/g) || []).length : e.length
                }, a.prototype.bindChangeEvents = function() {
                    var e = this.$textarea;
                    e.addEventListener("keyup", this.checkIfValueChanged.bind(this)), e.addEventListener("focus", this.handleFocus.bind(this)), e.addEventListener("blur", this.handleBlur.bind(this))
                }, a.prototype.checkIfValueChanged = function() {
                    this.$textarea.oldValue || (this.$textarea.oldValue = ""), this.$textarea.value !== this.$textarea.oldValue && (this.$textarea.oldValue = this.$textarea.value, this.updateCountMessage())
                }, a.prototype.updateCountMessage = function() {
                    var e = this.$textarea,
                        t = this.options,
                        n = this.$countMessage,
                        o = this.count(e.value),
                        i = this.maxLength,
                        r = i - o;
                    i * (t.threshold ? t.threshold : 0) / 100 > o ? (n.classList.add("govuk-character-count__message--disabled"), n.setAttribute("aria-hidden", !0)) : (n.classList.remove("govuk-character-count__message--disabled"), n.removeAttribute("aria-hidden")), r < 0 ? (e.classList.add("govuk-textarea--error"), n.classList.remove("govuk-hint"), n.classList.add("govuk-error-message")) : (e.classList.remove("govuk-textarea--error"), n.classList.remove("govuk-error-message"), n.classList.add("govuk-hint"));
                    var s, a, c = "character";
                    t.maxwords && (c = "word"), c += -1 === r || 1 === r ? "" : "s", s = r < 0 ? "too many" : "remaining", a = Math.abs(r), n.innerHTML = "You have " + a + " " + c + " " + s
                }, a.prototype.handleFocus = function() {
                    this.valueChecker = setInterval(this.checkIfValueChanged.bind(this), 1e3)
                }, a.prototype.handleBlur = function() {
                    clearInterval(this.valueChecker)
                }, c.prototype.init = function() {
                    var e = this.$module;
                    n(this.$inputs, (function(t) {
                        var n = t.getAttribute("data-aria-controls");
                        n && e.querySelector("#" + n) && (t.setAttribute("aria-controls", n), t.removeAttribute("data-aria-controls"))
                    })), "onpageshow" in window ? window.addEventListener("pageshow", this.syncAllConditionalReveals.bind(this)) : window.addEventListener("DOMContentLoaded", this.syncAllConditionalReveals.bind(this)), this.syncAllConditionalReveals(), e.addEventListener("click", this.handleClick.bind(this))
                }, c.prototype.syncAllConditionalReveals = function() {
                    n(this.$inputs, this.syncConditionalRevealWithInputState.bind(this))
                }, c.prototype.syncConditionalRevealWithInputState = function(e) {
                    var t = this.$module.querySelector("#" + e.getAttribute("aria-controls"));
                    if (t && t.classList.contains("govuk-checkboxes__conditional")) {
                        var n = e.checked;
                        e.setAttribute("aria-expanded", n), t.classList.toggle("govuk-checkboxes__conditional--hidden", !n)
                    }
                }, c.prototype.unCheckAllInputsExcept = function(e) {
                    n(document.querySelectorAll('input[type="checkbox"][name="' + e.name + '"]'), (function(t) {
                        e.form === t.form && t !== e && (t.checked = !1)
                    })), this.syncAllConditionalReveals()
                }, c.prototype.unCheckExclusiveInputs = function(e) {
                    n(document.querySelectorAll('input[data-behaviour="exclusive"][type="checkbox"][name="' + e.name + '"]'), (function(t) {
                        e.form === t.form && (t.checked = !1)
                    })), this.syncAllConditionalReveals()
                }, c.prototype.handleClick = function(e) {
                    var t = e.target;
                    "checkbox" === t.type && (t.getAttribute("aria-controls") && this.syncConditionalRevealWithInputState(t), t.checked && ("exclusive" === t.getAttribute("data-behaviour") ? this.unCheckAllInputsExcept(t) : this.unCheckExclusiveInputs(t)))
                },
                function(e) {
                    "document" in this && "matches" in document.documentElement || (Element.prototype.matches = Element.prototype.webkitMatchesSelector || Element.prototype.oMatchesSelector || Element.prototype.msMatchesSelector || Element.prototype.mozMatchesSelector || function(e) {
                        for (var t = (this.document || this.ownerDocument).querySelectorAll(e), n = 0; t[n] && t[n] !== this;) ++n;
                        return !!t[n]
                    })
                }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
                function(e) {
                    "document" in this && "closest" in document.documentElement || (Element.prototype.closest = function(e) {
                        for (var t = this; t;) {
                            if (t.matches(e)) return t;
                            t = "SVGElement" in window && t instanceof SVGElement ? t.parentNode : t.parentElement
                        }
                        return null
                    })
                }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}), u.prototype.init = function() {
                    var e = this.$module;
                    e && (e.focus(), e.addEventListener("click", this.handleClick.bind(this)))
                }, u.prototype.handleClick = function(e) {
                    var t = e.target;
                    this.focusTarget(t) && e.preventDefault()
                }, u.prototype.focusTarget = function(e) {
                    if ("A" !== e.tagName || !1 === e.href) return !1;
                    var t = this.getFragmentFromUrl(e.href),
                        n = document.getElementById(t);
                    if (!n) return !1;
                    var o = this.getAssociatedLegendOrLabel(n);
                    return !!o && (o.scrollIntoView(), n.focus({
                        preventScroll: !0
                    }), !0)
                }, u.prototype.getFragmentFromUrl = function(e) {
                    return -1 !== e.indexOf("#") && e.split("#").pop()
                }, u.prototype.getAssociatedLegendOrLabel = function(e) {
                    var t = e.closest("fieldset");
                    if (t) {
                        var n = t.getElementsByTagName("legend");
                        if (n.length) {
                            var o = n[0];
                            if ("checkbox" === e.type || "radio" === e.type) return o;
                            var i = o.getBoundingClientRect().top,
                                r = e.getBoundingClientRect();
                            if (r.height && window.innerHeight && r.top + r.height - i < window.innerHeight / 2) return o
                        }
                    }
                    return document.querySelector("label[for='" + e.getAttribute("id") + "']") || e.closest("label")
                }, l.prototype.init = function() {
                    this.$module && this.setFocus()
                }, l.prototype.setFocus = function() {
                    var e = this.$module;
                    "true" !== e.getAttribute("data-disable-auto-focus") && "alert" === e.getAttribute("role") && (e.getAttribute("tabindex") || (e.setAttribute("tabindex", "-1"), e.addEventListener("blur", (function() {
                        e.removeAttribute("tabindex")
                    }))), e.focus())
                }, d.prototype.init = function() {
                    this.$module && this.$menuButton && this.$menu && (this.syncState(this.$menu.classList.contains("govuk-header__navigation--open")), this.$menuButton.addEventListener("click", this.handleMenuButtonClick.bind(this)))
                }, d.prototype.syncState = function(e) {
                    this.$menuButton.classList.toggle("govuk-header__menu-button--open", e), this.$menuButton.setAttribute("aria-expanded", e)
                }, d.prototype.handleMenuButtonClick = function() {
                    var e = this.$menu.classList.toggle("govuk-header__navigation--open");
                    this.syncState(e)
                }, h.prototype.init = function() {
                    var e = this.$module;
                    n(this.$inputs, (function(t) {
                        var n = t.getAttribute("data-aria-controls");
                        n && e.querySelector("#" + n) && (t.setAttribute("aria-controls", n), t.removeAttribute("data-aria-controls"))
                    })), "onpageshow" in window ? window.addEventListener("pageshow", this.syncAllConditionalReveals.bind(this)) : window.addEventListener("DOMContentLoaded", this.syncAllConditionalReveals.bind(this)), this.syncAllConditionalReveals(), e.addEventListener("click", this.handleClick.bind(this))
                }, h.prototype.syncAllConditionalReveals = function() {
                    n(this.$inputs, this.syncConditionalRevealWithInputState.bind(this))
                }, h.prototype.syncConditionalRevealWithInputState = function(e) {
                    var t = document.querySelector("#" + e.getAttribute("aria-controls"));
                    if (t && t.classList.contains("govuk-radios__conditional")) {
                        var n = e.checked;
                        e.setAttribute("aria-expanded", n), t.classList.toggle("govuk-radios__conditional--hidden", !n)
                    }
                }, h.prototype.handleClick = function(e) {
                    var t = e.target;
                    "radio" === t.type && n(document.querySelectorAll('input[type="radio"][aria-controls]'), function(e) {
                        var n = e.form === t.form;
                        e.name === t.name && n && this.syncConditionalRevealWithInputState(e)
                    }.bind(this))
                },
                function(e) {
                    "document" in this && "nextElementSibling" in document.documentElement || Object.defineProperty(Element.prototype, "nextElementSibling", {
                        get: function() {
                            for (var e = this.nextSibling; e && 1 !== e.nodeType;) e = e.nextSibling;
                            return e
                        }
                    })
                }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
                function(e) {
                    "document" in this && "previousElementSibling" in document.documentElement || Object.defineProperty(Element.prototype, "previousElementSibling", {
                        get: function() {
                            for (var e = this.previousSibling; e && 1 !== e.nodeType;) e = e.previousSibling;
                            return e
                        }
                    })
                }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}), p.prototype.init = function() {
                    "function" == typeof window.matchMedia ? this.setupResponsiveChecks() : this.setup()
                }, p.prototype.setupResponsiveChecks = function() {
                    this.mql = window.matchMedia("(min-width: 40.0625em)"), this.mql.addListener(this.checkMode.bind(this)), this.checkMode()
                }, p.prototype.checkMode = function() {
                    this.mql.matches ? this.setup() : this.teardown()
                }, p.prototype.setup = function() {
                    var e = this.$module,
                        t = this.$tabs,
                        o = e.querySelector(".govuk-tabs__list"),
                        i = e.querySelectorAll(".govuk-tabs__list-item");
                    if (t && o && i) {
                        o.setAttribute("role", "tablist"), n(i, (function(e) {
                            e.setAttribute("role", "presentation")
                        })), n(t, function(e) {
                            this.setAttributes(e), e.boundTabClick = this.onTabClick.bind(this), e.boundTabKeydown = this.onTabKeydown.bind(this), e.addEventListener("click", e.boundTabClick, !0), e.addEventListener("keydown", e.boundTabKeydown, !0), this.hideTab(e)
                        }.bind(this));
                        var r = this.getTab(window.location.hash) || this.$tabs[0];
                        this.showTab(r), e.boundOnHashChange = this.onHashChange.bind(this), window.addEventListener("hashchange", e.boundOnHashChange, !0)
                    }
                }, p.prototype.teardown = function() {
                    var e = this.$module,
                        t = this.$tabs,
                        o = e.querySelector(".govuk-tabs__list"),
                        i = e.querySelectorAll(".govuk-tabs__list-item");
                    t && o && i && (o.removeAttribute("role"), n(i, (function(e) {
                        e.removeAttribute("role", "presentation")
                    })), n(t, function(e) {
                        e.removeEventListener("click", e.boundTabClick, !0), e.removeEventListener("keydown", e.boundTabKeydown, !0), this.unsetAttributes(e)
                    }.bind(this)), window.removeEventListener("hashchange", e.boundOnHashChange, !0))
                }, p.prototype.onHashChange = function(e) {
                    var t = window.location.hash,
                        n = this.getTab(t);
                    if (n)
                        if (this.changingHash) this.changingHash = !1;
                        else {
                            var o = this.getCurrentTab();
                            this.hideTab(o), this.showTab(n), n.focus()
                        }
                }, p.prototype.hideTab = function(e) {
                    this.unhighlightTab(e), this.hidePanel(e)
                }, p.prototype.showTab = function(e) {
                    this.highlightTab(e), this.showPanel(e)
                }, p.prototype.getTab = function(e) {
                    return this.$module.querySelector('.govuk-tabs__tab[href="' + e + '"]')
                }, p.prototype.setAttributes = function(e) {
                    var t = this.getHref(e).slice(1);
                    e.setAttribute("id", "tab_" + t), e.setAttribute("role", "tab"), e.setAttribute("aria-controls", t), e.setAttribute("aria-selected", "false"), e.setAttribute("tabindex", "-1");
                    var n = this.getPanel(e);
                    n.setAttribute("role", "tabpanel"), n.setAttribute("aria-labelledby", e.id), n.classList.add(this.jsHiddenClass)
                }, p.prototype.unsetAttributes = function(e) {
                    e.removeAttribute("id"), e.removeAttribute("role"), e.removeAttribute("aria-controls"), e.removeAttribute("aria-selected"), e.removeAttribute("tabindex");
                    var t = this.getPanel(e);
                    t.removeAttribute("role"), t.removeAttribute("aria-labelledby"), t.classList.remove(this.jsHiddenClass)
                }, p.prototype.onTabClick = function(e) {
                    if (!e.target.classList.contains("govuk-tabs__tab")) return !1;
                    e.preventDefault();
                    var t = e.target,
                        n = this.getCurrentTab();
                    this.hideTab(n), this.showTab(t), this.createHistoryEntry(t)
                }, p.prototype.createHistoryEntry = function(e) {
                    var t = this.getPanel(e),
                        n = t.id;
                    t.id = "", this.changingHash = !0, window.location.hash = this.getHref(e).slice(1), t.id = n
                }, p.prototype.onTabKeydown = function(e) {
                    switch (e.keyCode) {
                        case this.keys.left:
                        case this.keys.up:
                            this.activatePreviousTab(), e.preventDefault();
                            break;
                        case this.keys.right:
                        case this.keys.down:
                            this.activateNextTab(), e.preventDefault()
                    }
                }, p.prototype.activateNextTab = function() {
                    var e = this.getCurrentTab(),
                        t = e.parentNode.nextElementSibling;
                    if (t) var n = t.querySelector(".govuk-tabs__tab");
                    n && (this.hideTab(e), this.showTab(n), n.focus(), this.createHistoryEntry(n))
                }, p.prototype.activatePreviousTab = function() {
                    var e = this.getCurrentTab(),
                        t = e.parentNode.previousElementSibling;
                    if (t) var n = t.querySelector(".govuk-tabs__tab");
                    n && (this.hideTab(e), this.showTab(n), n.focus(), this.createHistoryEntry(n))
                }, p.prototype.getPanel = function(e) {
                    return this.$module.querySelector(this.getHref(e))
                }, p.prototype.showPanel = function(e) {
                    this.getPanel(e).classList.remove(this.jsHiddenClass)
                }, p.prototype.hidePanel = function(e) {
                    this.getPanel(e).classList.add(this.jsHiddenClass)
                }, p.prototype.unhighlightTab = function(e) {
                    e.setAttribute("aria-selected", "false"), e.parentNode.classList.remove("govuk-tabs__list-item--selected"), e.setAttribute("tabindex", "-1")
                }, p.prototype.highlightTab = function(e) {
                    e.setAttribute("aria-selected", "true"), e.parentNode.classList.add("govuk-tabs__list-item--selected"), e.setAttribute("tabindex", "0")
                }, p.prototype.getCurrentTab = function() {
                    return this.$module.querySelector(".govuk-tabs__list-item--selected .govuk-tabs__tab")
                }, p.prototype.getHref = function(e) {
                    var t = e.getAttribute("href");
                    return t.slice(t.indexOf("#"), t.length)
                }, t.initAll = function(e) {
                    var t = "undefined" != typeof(e = void 0 !== e ? e : {}).scope ? e.scope : document;
                    n(t.querySelectorAll('[data-module="govuk-button"]'), (function(e) {
                        new r(e).init()
                    })), n(t.querySelectorAll('[data-module="govuk-accordion"]'), (function(e) {
                        new o(e).init()
                    })), n(t.querySelectorAll('[data-module="govuk-details"]'), (function(e) {
                        new s(e).init()
                    })), n(t.querySelectorAll('[data-module="govuk-character-count"]'), (function(e) {
                        new a(e).init()
                    })), n(t.querySelectorAll('[data-module="govuk-checkboxes"]'), (function(e) {
                        new c(e).init()
                    })), new u(t.querySelector('[data-module="govuk-error-summary"]')).init(), new d(t.querySelector('[data-module="govuk-header"]')).init(), n(t.querySelectorAll('[data-module="govuk-notification-banner"]'), (function(e) {
                        new l(e).init()
                    })), n(t.querySelectorAll('[data-module="govuk-radios"]'), (function(e) {
                        new h(e).init()
                    })), n(t.querySelectorAll('[data-module="govuk-tabs"]'), (function(e) {
                        new p(e).init()
                    }))
                }, t.Accordion = o, t.Button = r, t.Details = s, t.CharacterCount = a, t.Checkboxes = c, t.ErrorSummary = u, t.Header = d, t.Radios = h, t.Tabs = p
        }(n)
    }));

    function o(e) {
        return (o = "function" == typeof Symbol && "symbol" == typeof Symbol.iterator ? function(e) {
            return typeof e
        } : function(e) {
            return e && "function" == typeof Symbol && e.constructor === Symbol && e !== Symbol.prototype ? "symbol" : typeof e
        })(e)
    }

    function i(e, t, n) {
        return t in e ? Object.defineProperty(e, t, {
            value: n,
            enumerable: !0,
            configurable: !0,
            writable: !0
        }) : e[t] = n, e
    }

    function r(e, t) {
        var n = Object.keys(e);
        if (Object.getOwnPropertySymbols) {
            var o = Object.getOwnPropertySymbols(e);
            t && (o = o.filter((function(t) {
                return Object.getOwnPropertyDescriptor(e, t).enumerable
            }))), n.push.apply(n, o)
        }
        return n
    }
    t((function(t, n) {
        (function(e) {
            var t, n, o;
            "defineProperty" in Object && function() {
                try {
                    return Object.defineProperty({}, "test", {
                        value: 42
                    }), !0
                } catch (e) {
                    return !1
                }
            }() || (t = Object.defineProperty, n = Object.prototype.hasOwnProperty("__defineGetter__"), o = "A property cannot both have accessors and be writable or have a value", Object.defineProperty = function(e, i, r) {
                if (t && (e === window || e === document || e === Element.prototype || e instanceof Element)) return t(e, i, r);
                if (null === e || !(e instanceof Object || "object" == typeof e)) throw new TypeError("Object.defineProperty called on non-object");
                if (!(r instanceof Object)) throw new TypeError("Property description must be an object");
                var s = String(i),
                    a = "value" in r || "writable" in r,
                    c = "get" in r && typeof r.get,
                    u = "set" in r && typeof r.set;
                if (c) {
                    if ("function" !== c) throw new TypeError("Getter must be a function");
                    if (!n) throw new TypeError("Getters & setters cannot be defined on this javascript engine");
                    if (a) throw new TypeError(o);
                    Object.__defineGetter__.call(e, s, r.get)
                } else e[s] = r.value;
                if (u) {
                    if ("function" !== u) throw new TypeError("Setter must be a function");
                    if (!n) throw new TypeError("Getters & setters cannot be defined on this javascript engine");
                    if (a) throw new TypeError(o);
                    Object.__defineSetter__.call(e, s, r.set)
                }
                return "value" in r && (e[s] = r.value), e
            })
        }).call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {})
    })), t((function(t, n) {
        (function(e) {
            var t, n, o;
            "defineProperty" in Object && function() {
                try {
                    return Object.defineProperty({}, "test", {
                        value: 42
                    }), !0
                } catch (e) {
                    return !1
                }
            }() || (t = Object.defineProperty, n = Object.prototype.hasOwnProperty("__defineGetter__"), o = "A property cannot both have accessors and be writable or have a value", Object.defineProperty = function(e, i, r) {
                if (t && (e === window || e === document || e === Element.prototype || e instanceof Element)) return t(e, i, r);
                if (null === e || !(e instanceof Object || "object" == typeof e)) throw new TypeError("Object.defineProperty called on non-object");
                if (!(r instanceof Object)) throw new TypeError("Property description must be an object");
                var s = String(i),
                    a = "value" in r || "writable" in r,
                    c = "get" in r && typeof r.get,
                    u = "set" in r && typeof r.set;
                if (c) {
                    if ("function" !== c) throw new TypeError("Getter must be a function");
                    if (!n) throw new TypeError("Getters & setters cannot be defined on this javascript engine");
                    if (a) throw new TypeError(o);
                    Object.__defineGetter__.call(e, s, r.get)
                } else e[s] = r.value;
                if (u) {
                    if ("function" !== u) throw new TypeError("Setter must be a function");
                    if (!n) throw new TypeError("Getters & setters cannot be defined on this javascript engine");
                    if (a) throw new TypeError(o);
                    Object.__defineSetter__.call(e, s, r.set)
                }
                return "value" in r && (e[s] = r.value), e
            })
        }).call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
            function(e) {
                "bind" in Function.prototype || Object.defineProperty(Function.prototype, "bind", {
                    value: function(e) {
                        var t, n = Array,
                            o = Object,
                            i = o.prototype,
                            r = n.prototype,
                            s = function() {},
                            a = i.toString,
                            c = "function" == typeof Symbol && "symbol" == typeof Symbol.toStringTag,
                            u = Function.prototype.toString,
                            l = function(e) {
                                try {
                                    return u.call(e), !0
                                } catch (t) {
                                    return !1
                                }
                            },
                            d = "[object Function]",
                            h = "[object GeneratorFunction]";
                        t = function(e) {
                            if ("function" != typeof e) return !1;
                            if (c) return l(e);
                            var t = a.call(e);
                            return t === d || t === h
                        };
                        var p = r.slice,
                            f = r.concat,
                            m = r.push,
                            b = Math.max,
                            v = this;
                        if (!t(v)) throw new TypeError("Function.prototype.bind called on incompatible " + v);
                        for (var y, w = p.call(arguments, 1), g = function() {
                                if (this instanceof y) {
                                    var t = v.apply(this, f.call(w, p.call(arguments)));
                                    return o(t) === t ? t : this
                                }
                                return v.apply(e, f.call(w, p.call(arguments)))
                            }, E = b(0, v.length - w.length), L = [], k = 0; k < E; k++) m.call(L, "$" + k);
                        return y = Function("binder", "return function (" + L.join(",") + "){ return binder.apply(this, arguments); }")(g), v.prototype && (s.prototype = v.prototype, y.prototype = new s, s.prototype = null), y
                    }
                })
            }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {})
    }));
    (function(e) {
        "getOwnPropertyDescriptor" in Object && "function" == typeof Object.getOwnPropertyDescriptor && function() {
            try {
                var e = {
                    test: 0
                };
                return 0 === Object.getOwnPropertyDescriptor(e, "test").value
            } catch (t) {
                return !1
            }
        }() || function() {
            var e, t, n, i = Function.prototype.call,
                r = Object.prototype,
                s = i.bind(r.hasOwnProperty);

            function a(e) {
                try {
                    return e.sentinel = 0, 0 === Object.getOwnPropertyDescriptor(e, "sentinel").value
                } catch (t) {}
            }
            if ((n = s(r, "__defineGetter__")) && (e = i.bind(r.__lookupGetter__), t = i.bind(r.__lookupSetter__)), Object.defineProperty) {
                var c = a({});
                if (!("undefined" == typeof document || a(document.createElement("div"))) || !c) var u = Object.getOwnPropertyDescriptor
            }
            if (!Object.getOwnPropertyDescriptor || u) {
                Object.getOwnPropertyDescriptor = function(i, a) {
                    if ("object" != o(i) && "function" != typeof i || null === i) throw new TypeError("Object.getOwnPropertyDescriptor called on a non-object: " + i);
                    if (u) try {
                        return u.call(Object, i, a)
                    } catch (p) {}
                    if (s(i, a)) {
                        var c = {
                            enumerable: !0,
                            configurable: !0
                        };
                        if (n) {
                            var l = i.__proto__;
                            i.__proto__ = r;
                            var d = e(i, a),
                                h = t(i, a);
                            if (i.__proto__ = l, d || h) return d && (c.get = d), h && (c.set = h), c
                        }
                        return c.value = i[a], c.writable = !0, c
                    }
                }
            }
        }()
    }).call("object" === ("undefined" == typeof window ? "undefined" : o(window)) && window || "object" === ("undefined" == typeof self ? "undefined" : o(self)) && self || "object" === ("undefined" == typeof global ? "undefined" : o(global)) && global || {});
    t((function(t, n) {
        (function(e) {
            "Document" in this || "undefined" == typeof WorkerGlobalScope && "function" != typeof importScripts && (this.HTMLDocument ? this.Document = this.HTMLDocument : (this.Document = this.HTMLDocument = document.constructor = new Function("return function Document() {}")(), this.Document.prototype = document))
        }).call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {})
    })), t((function(t, n) {
        (function(e) {
            "Document" in this || "undefined" == typeof WorkerGlobalScope && "function" != typeof importScripts && (this.HTMLDocument ? this.Document = this.HTMLDocument : (this.Document = this.HTMLDocument = document.constructor = new Function("return function Document() {}")(), this.Document.prototype = document))
        }).call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
            function(e) {
                "Element" in this && "HTMLElement" in this || function() {
                    if (!window.Element || window.HTMLElement) {
                        window.Element = window.HTMLElement = new Function("return function Element() {}")();
                        var e, t = document.appendChild(document.createElement("body")),
                            n = t.appendChild(document.createElement("iframe")).contentWindow.document,
                            o = Element.prototype = n.appendChild(n.createElement("*")),
                            i = {},
                            r = function(e, t) {
                                var n, o, s, a = e.childNodes || [],
                                    c = -1;
                                if (1 === e.nodeType && e.constructor !== Element)
                                    for (n in e.constructor = Element, i) o = i[n], e[n] = o;
                                for (; s = t && a[++c];) r(s, t);
                                return e
                            },
                            s = document.getElementsByTagName("*"),
                            a = document.createElement,
                            c = 100;
                        o.attachEvent("onpropertychange", (function(e) {
                            for (var t, n = e.propertyName, r = !i.hasOwnProperty(n), a = o[n], c = i[n], u = -1; t = s[++u];) 1 === t.nodeType && (r || t[n] === c) && (t[n] = a);
                            i[n] = a
                        })), o.constructor = Element, o.hasAttribute || (o.hasAttribute = function(e) {
                            return null !== this.getAttribute(e)
                        }), u() || (document.onreadystatechange = u, e = setInterval(u, 25)), document.createElement = function(e) {
                            var t = a(String(e).toLowerCase());
                            return r(t)
                        }, document.removeChild(t)
                    } else window.HTMLElement = window.Element;

                    function u() {
                        return c-- || clearTimeout(e), !(!document.body || document.body.prototype || !/(complete|interactive)/.test(document.readyState) || (r(document, !0), e && document.body.prototype && clearTimeout(e), !document.body.prototype))
                    }
                }()
            }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {})
    }));
    (function(e) {
        "document" in this && "querySelector" in this.document || function() {
            var e = document.getElementsByTagName("head")[0];

            function t(t, n, o) {
                var i, r, s = document.createElement("div"),
                    a = "qsa" + String(Math.random()).slice(3);
                return s.innerHTML = "x<style>" + n + "{qsa:" + a + ";}", i = e.appendChild(s.lastChild), r = function c(e, t, n, o) {
                    var i, r = /1|9/.test(e.nodeType),
                        s = e.childNodes,
                        a = [],
                        u = -1;
                    if (r && e.currentStyle && e.currentStyle.qsa === o && a.push(e) && n) return a;
                    for (; i = s[++u];)
                        if (a = a.concat(c(i, t, n, o)), n && a.length) return a;
                    return a
                }(t, n, o, a), e.removeChild(i), o ? r[0] : r
            }
            Document.prototype.querySelector = Element.prototype.querySelector = function(e) {
                return t(this, e, !0)
            }, Document.prototype.querySelectorAll = Element.prototype.querySelectorAll = function(e) {
                return t(this, e, !1)
            }
        }()
    }).call("object" === ("undefined" == typeof window ? "undefined" : o(window)) && window || "object" === ("undefined" == typeof self ? "undefined" : o(self)) && self || "object" === ("undefined" == typeof global ? "undefined" : o(global)) && global || {}),
        function(e) {
            ! function() {
                if (!document.documentElement.dataset) return !1;
                var e = document.createElement("div");
                e.setAttribute("data-a-b", "c"), e.dataset && e.dataset.aB
            }(), Object.defineProperty(Element.prototype, "dataset", {
                get: function() {
                    for (var e = this.attributes, t = {}, n = 0; n < e.length; n++) {
                        var o = e[n];
                        if (o && o.name && /^data-\w[\w\-]*$/.test(o.name)) {
                            var i = o.name,
                                r = o.value,
                                s = i.substr(5).replace(/-./g, (function(e) {
                                    return e.charAt(1).toUpperCase()
                                }));
                            Object.defineProperty(t, s, {
                                enumerable: this.enumerable,
                                get: function() {
                                    return this.value
                                }.bind({
                                    value: r || ""
                                }),
                                set: function(e, t) {
                                    void 0 !== t ? this.setAttribute(e, t) : this.removeAttribute(e)
                                }.bind(this, i)
                            })
                        }
                    }
                    return t
                }
            })
        }.call("object" === ("undefined" == typeof window ? "undefined" : o(window)) && window || "object" === ("undefined" == typeof self ? "undefined" : o(self)) && self || "object" === ("undefined" == typeof global ? "undefined" : o(global)) && global || {});
    var s = {
        xs: 0,
        mobile: 320,
        tablet: 641,
        desktop: 769
    };

    function a(e) {
        return Object.keys(s).reduce((function(t, n) {
            return (e || window.innerWidth) >= s[n] ? n : t
        }))
    }
    var c = function(e) {
        return e.innerWidth <= 768
    };

    function u(e) {
        this.$module = document.querySelector(e), this.$moduleBottomMargin = this.$module.style.marginBottom, this.$mainNav = this.$module.querySelector(".hmrc-account-menu__main"), this.$subNav = this.$module.querySelector(".hmrc-subnav"), this.$showSubnavLink = this.$module.querySelector("#account-menu__main-2"), this.$showNavLinkMobile = this.$module.querySelector(".hmrc-account-menu__link--menu"), this.$backLink = this.$module.querySelector(".hmrc-account-menu__link--back a"), this.$currentBreakpoint = a()
    }

    function l(e, t) {
        if (window.NodeList.prototype.forEach) return e.forEach(t);
        for (var n = 0; n < e.length; n += 1) t.call(window, e[n], n, e)
    }
    u.prototype.init = function() {
            this.setup(), this.$showSubnavLink.addEventListener("click", this.eventHandlers.showSubnavLinkClick.bind(this)), this.$showSubnavLink.addEventListener("focusout", this.eventHandlers.showSubnavLinkFocusOut.bind(this)), this.$showSubnavLink.addEventListener("focusin", this.eventHandlers.showSubnavLinkFocusIn.bind(this)), this.$backLink.addEventListener("click", this.eventHandlers.backLinkClick.bind(this)), this.$subNav.addEventListener("focusout", this.eventHandlers.subNavFocusOut.bind(this)), this.$subNav.addEventListener("focusin", this.eventHandlers.subNavFocusIn.bind(this)), this.$showNavLinkMobile.addEventListener("click", this.eventHandlers.showNavLinkMobileClick.bind(this)), window.addEventListener("resize", function(e, t, n) {
                var o, i = this;
                return function() {
                    for (var r = arguments.length, s = new Array(r), a = 0; a < r; a++) s[a] = arguments[a];
                    var c = i,
                        u = function() {
                            o = null, n || e.apply(c, s)
                        },
                        l = n && !o;
                    clearTimeout(o), o = setTimeout(u, t), l && e.apply(c, s)
                }
            }(this.reinstantiate.bind(this)))
        }, u.prototype.reinstantiate = function(e) {
            var t = a(e.target.innerWidth);
            this.$currentBreakpoint !== t && (this.$currentBreakpoint = t, this.setup())
        }, u.prototype.eventHandlers = {
            showSubnavLinkClick: function(e) {
                e.preventDefault(), e.stopPropagation(), c(window) ? e.currentTarget.classList.contains("hmrc-account-menu__link--more-expanded") || (this.hideMainNavMobile(e.currentTarget), this.showSubnavMobile(e.currentTarget)) : e.currentTarget.classList.contains("hmrc-account-menu__link--more-expanded") ? this.hideSubnavDesktop() : this.showSubnavDesktop()
            },
            showSubnavLinkFocusOut: function(e) {
                c(window) || (this.$module.querySelector(e.target.hash).dataset.subMenuTimer = setTimeout((function() {}), 0))
            },
            showSubnavLinkFocusIn: function(e) {
                c(window) || clearTimeout(this.$module.querySelector(e.target.hash).dataset.subMenuTimer)
            },
            backLinkClick: function(e) {
                e.preventDefault(), this.$mainNav.classList.contains("hmrc-subnav-is-open") && (this.hideSubnavMobile(), this.showMainNavMobile())
            },
            subNavFocusOut: function(e) {
                c(window) || (e.currentTarget.dataset.subMenuTimer = setTimeout(this.hideSubnavDesktop.bind(this), 0))
            },
            subNavFocusIn: function(e) {
                clearTimeout(e.currentTarget.dataset.subMenuTimer)
            },
            showNavLinkMobileClick: function(e) {
                e.preventDefault(), c(window) && (this.$mainNav.classList.contains("hmrc-subnav-is-open") || this.$mainNav.classList.contains("main-nav-is-open") ? (this.hideSubnavMobile(), this.hideMainNavMobile(e.currentTarget)) : this.showMainNavMobile())
            }
        }, u.prototype.setup = function() {
            this.$subNav.setAttribute("aria-hidden", "true"), this.$subNav.setAttribute("tabindex", "-1"), this.$showSubnavLink.setAttribute("aria-controls", this.$showSubnavLink.hash.substr(1)), this.$showSubnavLink.setAttribute("aria-expanded", "false"), c(window) ? (this.$module.classList.add("is-smaller"), this.$showNavLinkMobile.setAttribute("aria-hidden", "false"), this.$showNavLinkMobile.removeAttribute("tabindex"), this.$showNavLinkMobile.classList.remove("js-hidden"), this.hideSubnavMobile(), this.hideMainNavMobile(this.$showNavLinkMobile)) : (this.$module.classList.remove("is-smaller"), this.$mainNav.classList.remove("main-nav-is-open", "js-hidden"), this.$subNav.classList.remove("js-hidden"), this.$showNavLinkMobile.setAttribute("aria-hidden", "true"), this.$showNavLinkMobile.setAttribute("tabindex", "-1"), this.$showNavLinkMobile.classList.add("js-hidden"))
        }, u.prototype.showSubnavDesktop = function() {
            var e = this;
            this.$module.classList.add("hmrc-subnav-is-open"), this.$mainNav.classList.add("hmrc-subnav-is-open"), this.$subNav.classList.add("hmrc-subnav-reveal"), this.$subNav.setAttribute("aria-hidden", "false"), this.$subNav.setAttribute("aria-expanded", "true");
            var t = this.$subNav.offsetHeight;
            this.$module.style.marginBottom = "".concat(t, "px"), setTimeout((function() {
                e.$subNav.focus()
            }), 500), this.$showSubnavLink.classList.add("hmrc-account-menu__link--more-expanded"), this.$showSubnavLink.setAttribute("aria-expanded", "true")
        }, u.prototype.hideSubnavDesktop = function() {
            this.$module.classList.remove("main-nav-is-open", "hmrc-subnav-is-open"), this.$mainNav.classList.remove("hmrc-subnav-is-open"), this.$subNav.classList.remove("hmrc-subnav-reveal"), this.$subNav.setAttribute("aria-hidden", "true"), this.$subNav.setAttribute("aria-expanded", "false"), this.$showSubnavLink.classList.remove("hmrc-account-menu__link--more-expanded"), this.$showSubnavLink.setAttribute("aria-expanded", "false"), this.$module.style.marginBottom = this.$moduleBottomMargin
        }, u.prototype.showMainNavMobile = function() {
            this.$mainNav.classList.remove("js-hidden"), this.$mainNav.classList.add("main-nav-is-open"), this.$mainNav.setAttribute("aria-expanded", "true"), this.$showNavLinkMobile.setAttribute("aria-expanded", "true"), this.$showNavLinkMobile.classList.add("hmrc-account-home--account--is-open")
        }, u.prototype.hideMainNavMobile = function(e) {
            this.$mainNav.classList.remove("main-nav-is-open"), this.$mainNav.setAttribute("aria-expanded", "false"), e.classList.contains("hmrc-account-menu__link--menu") ? (this.$mainNav.classList.remove("hmrc-subnav-is-open"), this.$mainNav.classList.add("js-hidden"), this.$showNavLinkMobile.setAttribute("aria-expanded", "false"), this.$showNavLinkMobile.classList.remove("hmrc-account-home--account--is-open")) : e.classList.contains("hmrc-account-menu__link--more") && this.$mainNav.classList.add("hmrc-subnav-is-open")
        }, u.prototype.showSubnavMobile = function(e) {
            this.$module.classList.add("hmrc-subnav-is-open"), this.$mainNav.classList.remove("main-nav-is-open"), this.$mainNav.classList.add("hmrc-subnav-is-open"), this.$subNav.classList.remove("js-hidden"), this.$subNav.classList.add("hmrc-subnav-reveal"), this.$subNav.setAttribute("aria-hidden", "false"), this.$subNav.setAttribute("aria-expanded", "true"), this.$showSubnavLink.classList.add("hmrc-account-menu__link--more-expanded"), this.$showSubnavLink.setAttribute("aria-expanded", "true"), this.$backLink.parentNode.setAttribute("aria-hidden", "false"), this.$backLink.removeAttribute("tabindex"), this.$backLink.parentNode.classList.remove("hidden"), e.parentNode.classList.add("active-subnav-parent");
            for (var t = e.parentNode.parentNode.children, n = 0; n < t.length; n += 1) {
                var o = t[n];
                o !== this.$backLink.parentNode && o !== e.parentNode && o.classList.add("hidden")
            }
        }, u.prototype.hideSubnavMobile = function() {
            this.$module.classList.remove("hmrc-subnav-is-open"), this.$mainNav.classList.remove("hmrc-subnav-is-open"), this.$mainNav.classList.add("main-nav-is-open"), this.$subNav.classList.add("js-hidden"), this.$subNav.classList.remove("hmrc-subnav-reveal"), this.$subNav.setAttribute("aria-hidden", "true"), this.$subNav.setAttribute("aria-expanded", "false"), this.$showSubnavLink.classList.remove("hmrc-account-menu__link--more-expanded"), this.$showSubnavLink.setAttribute("aria-expanded", "false"), this.$backLink.parentNode.setAttribute("aria-hidden", "true"), this.$backLink.setAttribute("tabindex", "-1"), this.$backLink.parentNode.classList.add("hidden"), this.$showSubnavLink.parentNode.classList.remove("active-subnav-parent");
            for (var e = this.$backLink.parentNode.parentNode.children, t = 0; t < e.length; t += 1) {
                var n = e[t];
                n !== this.$backLink.parentNode && n.classList.remove("hidden")
            }
        },
        function(e) {
            "forEach" in Array.prototype || (Array.prototype.forEach = function(t) {
                if (this === e || null === this) throw new TypeError(this + " is not an object");
                if ("function" != typeof t) throw new TypeError(t + " is not a function");
                for (var n = Object(this), o = arguments[1], i = n instanceof String ? n.split("") : n, r = Math.max(Math.min(i.length, 9007199254740991), 0) || 0, s = -1; ++s < r;) s in i && t.call(o, i[s], s, n)
            })
        }.call("object" === ("undefined" == typeof window ? "undefined" : o(window)) && window || "object" === ("undefined" == typeof self ? "undefined" : o(self)) && self || "object" === ("undefined" == typeof global ? "undefined" : o(global)) && global || {}),
        function(e) {
            var t, n, i, r, s, a, c, u, l;
            "keys" in Object && function() {
                return 2 === Object.keys(arguments).length
            }(1, 2) && function() {
                try {
                    return !0
                } catch (e) {
                    return !1
                }
            }() || (Object.keys = (t = Object.prototype.hasOwnProperty, n = Object.prototype.toString, i = Object.prototype.propertyIsEnumerable, r = !i.call({
                toString: null
            }, "toString"), s = i.call((function() {}), "prototype"), a = ["toString", "toLocaleString", "valueOf", "hasOwnProperty", "isPrototypeOf", "propertyIsEnumerable", "constructor"], c = function(e) {
                var t = e.constructor;
                return t && t.prototype === e
            }, u = {
                $console: !0,
                $external: !0,
                $frame: !0,
                $frameElement: !0,
                $frames: !0,
                $innerHeight: !0,
                $innerWidth: !0,
                $outerHeight: !0,
                $outerWidth: !0,
                $pageXOffset: !0,
                $pageYOffset: !0,
                $parent: !0,
                $scrollLeft: !0,
                $scrollTop: !0,
                $scrollX: !0,
                $scrollY: !0,
                $self: !0,
                $webkitIndexedDB: !0,
                $webkitStorageInfo: !0,
                $window: !0
            }, l = function() {
                if ("undefined" == typeof window) return !1;
                for (var e in window) try {
                    if (!u["$" + e] && t.call(window, e) && null !== window[e] && "object" === o(window[e])) try {
                        c(window[e])
                    } catch (n) {
                        return !0
                    }
                } catch (n) {
                    return !0
                }
                return !1
            }(), function(i) {
                var u, d, h, p = "[object Function]" === n.call(i),
                    f = (u = i, d = n.call(u), (h = "[object Arguments]" === d) || (h = "[object Array]" !== d && null !== u && "object" === o(u) && "number" == typeof u.length && u.length >= 0 && "[object Function]" === n.call(u.callee)), h),
                    m = "[object String]" === n.call(i),
                    b = [];
                if (i === e || null === i) throw new TypeError("Cannot convert undefined or null to object");
                var v = s && p;
                if (m && i.length > 0 && !t.call(i, 0))
                    for (var y = 0; y < i.length; ++y) b.push(String(y));
                if (f && i.length > 0)
                    for (var w = 0; w < i.length; ++w) b.push(String(w));
                else
                    for (var g in i) v && "prototype" === g || !t.call(i, g) || b.push(String(g));
                if (r)
                    for (var E = function(e) {
                            if ("undefined" == typeof window || !l) return c(e);
                            try {
                                return c(e)
                            } catch (t) {
                                return !1
                            }
                        }(i), L = 0; L < a.length; ++L) E && "constructor" === a[L] || !t.call(i, a[L]) || b.push(a[L]);
                return b
            }))
        }.call("object" === ("undefined" == typeof window ? "undefined" : o(window)) && window || "object" === ("undefined" == typeof self ? "undefined" : o(self)) && self || "object" === ("undefined" == typeof global ? "undefined" : o(global)) && global || {}),
        function(e) {
            "assign" in Object || function() {
                function t(t) {
                    if (null === t || t === e) throw new TypeError("Cannot call method on " + t);
                    return Object(t)
                }
                Object.defineProperty(Object, "assign", {
                    enumerable: !1,
                    configurable: !0,
                    writable: !0,
                    value: function(n, o) {
                        var i = t(n);
                        if (1 === arguments.length) return i;
                        var r, s, a, c, u = Array.prototype.slice.call(arguments, 1);
                        for (r = 0; r < u.length; r++) {
                            var l = u[r];
                            for (l === e || null === l ? a = [] : (c = t(l), a = Object.keys(c)), s = 0; s < a.length; s++) {
                                var d = a[s],
                                    h = Object.getOwnPropertyDescriptor(c, d);
                                if (h !== e && h.enumerable) {
                                    var p = c[d];
                                    i[d] = p
                                }
                            }
                        }
                        return i
                    }
                })
            }()
        }.call("object" === ("undefined" == typeof window ? "undefined" : o(window)) && window || "object" === ("undefined" == typeof self ? "undefined" : o(self)) && self || "object" === ("undefined" == typeof global ? "undefined" : o(global)) && global || {}),
        function(e) {
            "Date" in this && "now" in this.Date && "getTime" in this.Date.prototype || (Date.now = function() {
                return (new Date).getTime()
            })
        }.call("object" === ("undefined" == typeof window ? "undefined" : o(window)) && window || "object" === ("undefined" == typeof self ? "undefined" : o(self)) && self || "object" === ("undefined" == typeof global ? "undefined" : o(global)) && global || {});
    var d = console.warn,
        h = {
            generateDomElementFromString: function(e) {
                var t = document.createElement("div");
                return t.innerHTML = e, t.firstChild
            },
            generateDomElementFromStringAndAppendText: function(e, t) {
                var n = h.generateDomElementFromString(e);
                return n.innerText = t, n
            },
            hasClass: function(e, t) {
                return document.querySelector(e).classList.contains(t)
            },
            addClass: function(e, t) {
                l(document.querySelectorAll(e), (function(e) {
                    e.classList.add(t)
                }))
            },
            removeClass: function(e, t) {
                l(document.querySelectorAll(e), (function(e) {
                    e.classList.remove(t)
                }))
            },
            removeElement: function(e) {
                var t = e.parentNode;
                t ? t.removeChild(e) : d("couldn't find parent for elem", e)
            },
            ajaxGet: function(e, t) {
                var n = window.XMLHttpRequest ? new XMLHttpRequest : new ActiveXObject("Microsoft.XMLHTTP");
                return n.open("GET", e), n.onreadystatechange = function() {
                    n.readyState > 3 && 200 === n.status && t(n.responseText)
                }, n.setRequestHeader("X-Requested-With", "XMLHttpRequest"), n.send(), n
            }
        };
    var p = {
        displayDialog: function(e) {
            var t = h.generateDomElementFromString('<div id="hmrc-timeout-dialog" tabindex="-1" role="dialog" class="hmrc-timeout-dialog">'),
                n = h.generateDomElementFromString('<div id="hmrc-timeout-overlay" class="hmrc-timeout-overlay">'),
                o = "string" == typeof e ? h.generateDomElementFromString(e) : e,
                i = [],
                r = [];
            t.appendChild(o), h.hasClass("html", "noScroll") || (h.addClass("html", "noScroll"), i.push((function() {
                h.removeClass("html", "noScroll")
            }))), document.body.appendChild(t), document.body.appendChild(n), i.push((function() {
                h.removeElement(t), h.removeElement(n)
            }));
            var s, a = document.querySelectorAll(["#skiplink-container", "body > header", "#global-cookie-message", "main[role=main]", "body > footer"].join(", ")),
                c = function() {
                    for (; i.length > 0;) {
                        i.shift()()
                    }
                };
            return l(a, (function(e) {
                    var t = e.getAttribute("aria-hidden");
                    e.setAttribute("aria-hidden", "true"), i.push((function() {
                        t ? e.setAttribute("aria-hidden", t) : e.removeAttribute("aria-hidden")
                    }))
                })),
                function() {
                    function e(e) {
                        var t = document.getElementById("hmrc-timeout-dialog");
                        t && (e.target === t || t.contains(e.target) || (e.stopPropagation(), t.focus()))
                    }
                    var n = document.activeElement;
                    t.focus(), document.addEventListener("focus", e, !0), i.push((function() {
                        document.removeEventListener("focus", e), n.focus()
                    }))
                }(),
                function() {
                    function e(e) {
                        27 === e.keyCode && (r.forEach((function(e) {
                            e()
                        })), c())
                    }
                    document.addEventListener("keydown", e), i.push((function() {
                        document.removeEventListener("keydown", e)
                    }))
                }(), s = function(e) {
                    1 === (e.touches || e.changedTouches || []).length && e.preventDefault()
                }, document.addEventListener("touchmove", s, !0), i.push((function() {
                    document.removeEventListener("touchmove", s, !0)
                })), {
                    closeDialog: function() {
                        c()
                    },
                    setAriaLabelledBy: function(e) {
                        e ? t.setAttribute("aria-labelledby", e) : t.removeAttribute("aria-labelledby")
                    },
                    addCloseHandler: function(e) {
                        r.push(e)
                    }
                }
        }
    };

    function f() {}

    function m() {}

    function b(e) {
        var t, n = {},
            s = {},
            a = [];
        a.push((function() {
            t && window.clearTimeout(t)
        }));
        var c = function(e) {
                var t = [];
                if (["timeout", "countdown", "keepAliveUrl", "signOutUrl"].forEach((function(n) {
                        e[n] || t.push("data-".concat(n.replace(/([a-z])([A-Z])/g, "$1-$2").toLowerCase()))
                    })), t.length > 0) throw new Error("Missing config item(s): [".concat(t.join(", "), "]"))
            },
            u = function k(e, t) {
                var n = function(e) {
                    for (var t = 1; t < arguments.length; t++) {
                        var n = null != arguments[t] ? arguments[t] : {};
                        t % 2 ? r(Object(n), !0).forEach((function(t) {
                            i(e, t, n[t])
                        })) : Object.getOwnPropertyDescriptors ? Object.defineProperties(e, Object.getOwnPropertyDescriptors(n)) : r(Object(n)).forEach((function(t) {
                            Object.defineProperty(e, t, Object.getOwnPropertyDescriptor(n, t))
                        }))
                    }
                    return e
                }({}, e);
                return Object.keys(t).forEach((function(i) {
                    "object" === o(n[i]) && (n[i] = k(e[i], t[i])), n[i] !== undefined && "" !== n[i] || (n[i] = t[i])
                })), n
            },
            l = function() {
                s.signout_time = w() + 1e3 * s.timeout;
                var e = window.setTimeout((function() {
                    d()
                }), 1e3 * (s.timeout - s.countdown));
                a.push((function() {
                    window.clearTimeout(e)
                }))
            },
            d = function() {
                var e = h.generateDomElementFromString("<div>");
                if (s.title) {
                    var t = h.generateDomElementFromStringAndAppendText('<h1 class="govuk-heading-m push--top">', s.title);
                    e.appendChild(t)
                }
                var n = h.generateDomElementFromString('<span id="hmrc-timeout-countdown" class="hmrc-timeout-dialog__countdown">'),
                    o = h.generateDomElementFromString('<p id="hmrc-timeout-message" class="govuk-visually-hidden screenreader-content" aria-live="assertive">'),
                    i = h.generateDomElementFromStringAndAppendText('<p class="govuk-body hmrc-timeout-dialog__message" aria-hidden="true">', s.message);
                i.appendChild(document.createTextNode(" ")), i.appendChild(n), i.appendChild(document.createTextNode(".")), s.messageSuffix && i.appendChild(document.createTextNode(" ".concat(s.messageSuffix)));
                var r, c, u = h.generateDomElementFromStringAndAppendText('<button id="hmrc-timeout-keep-signin-btn" class="govuk-button">', s.keepAliveButtonText),
                    l = h.generateDomElementFromStringAndAppendText('<a id="hmrc-timeout-sign-out-link" class="govuk-link hmrc-timeout-dialog__link">', s.signOutButtonText);
                u.addEventListener("click", y), l.addEventListener("click", g), l.setAttribute("href", s.signOutUrl), e.appendChild(i), e.appendChild(o), e.appendChild(u), e.appendChild(document.createTextNode(" ")), e.appendChild((r = l, (c = document.createElement("div")).classList.add("hmrc-timeout-dialog__link-wrapper"), c.appendChild(r), c));
                var d = p.displayDialog(e);
                a.push((function() {
                    d.closeDialog()
                })), d.addCloseHandler(y), d.setAriaLabelledBy("hmrc-timeout-message"), v(n, o)
            },
            b = function() {
                return s.signout_time - w()
            },
            v = function(e, n) {
                var o = function(e) {
                        var t, n;
                        return e < 60 ? n = "".concat(e, " ").concat(s.properties[1 !== e ? "seconds" : "second"]) : (t = Math.ceil(e / 60), n = "".concat(t, " ").concat(s.properties[1 === t ? "minute" : "minutes"])), n
                    },
                    i = function(e) {
                        return e > 60 ? e : e < 20 ? 20 : 20 * Math.ceil(e / 20)
                    },
                    r = function(e, t) {
                        e.innerText !== t && (e.innerText = t)
                    },
                    a = function(t) {
                        var a = o(t),
                            c = function(e) {
                                var t = o(i(e)),
                                    n = [s.message, " ", t, "."];
                                return s.messageSuffix && (n.push(" "), n.push(s.messageSuffix)), n.join("")
                            }(t);
                        r(e, a), r(n, c)
                    };
                ! function c() {
                    var e, n, o = Math.round(b() / 1e3);
                    a(o), o <= 0 && E(), t = window.setTimeout(c, (e = b(), (n = 1e3 * Math.floor(b() / 1e3)) <= 6e4 ? e - n || 1e3 : e - (n - (n % 6e4 || 6e4))))
                }()
            },
            y = function() {
                L(), l(), h.ajaxGet(s.keepAliveUrl, (function() {}))
            },
            w = function() {
                return Date.now()
            },
            g = function() {
                m.redirectToUrl(s.signOutUrl)
            },
            E = function() {
                m.redirectToUrl(s.timeoutUrl)
            },
            L = function() {
                for (; a.length > 0;) {
                    a.shift()()
                }
            };
        return {
            init: function() {
                var t = f;

                function o(t) {
                    return (e.attributes.getNamedItem(t) || {}).value
                }
                var i = "cy" === t.string(o("data-language")) ? {
                    title: undefined,
                    message: "Er eich diogelwch, byddwn yn eich allgofnodi cyn pen",
                    keepAliveButtonText: "Parhau i fod wedi’ch mewngofnodi",
                    signOutButtonText: "Allgofnodi",
                    properties: {
                        minutes: "funud",
                        minute: "funud",
                        seconds: "eiliad",
                        second: "eiliad"
                    }
                } : {
                    title: undefined,
                    message: "For your security, we will sign you out in",
                    keepAliveButtonText: "Stay signed in",
                    signOutButtonText: "Sign out",
                    properties: {
                        minutes: "minutes",
                        minute: "minute",
                        seconds: "seconds",
                        second: "second"
                    }
                };
                (n = {
                    timeout: t.int(o("data-timeout")),
                    countdown: t.int(o("data-countdown")),
                    keepAliveUrl: t.string(o("data-keep-alive-url")),
                    signOutUrl: t.string(o("data-sign-out-url")),
                    timeoutUrl: t.string(o("data-timeout-url")),
                    title: t.string(o("data-title")),
                    message: t.string(o("data-message")),
                    messageSuffix: t.string(o("data-message-suffix")),
                    keepAliveButtonText: t.string(o("data-keep-alive-button-text")),
                    signOutButtonText: t.string(o("data-sign-out-button-text"))
                }).timeoutUrl = n.timeoutUrl || n.signOutUrl, c(n), s = u(n, i), l()
            },
            cleanup: L
        }
    }
    Number.isNaN = Number.isNaN || function(e) {
        return "number" == typeof e && e != e
    }, f.int = function(e) {
        var t = parseInt(e, 10);
        return Number.isNaN(t) ? undefined : t
    }, f.string = function(e) {
        return e && ("".concat(e) || undefined)
    }, m.redirectToUrl = function(e) {
        window.location.href = e
    }, b.dialog = p, b.redirectHelper = m, b.utils = h;

    function v(e) {
        this.$module = e, this.$closeLink = this.$module.querySelector(".hmrc-user-research-banner__close"), this.cookieName = "mdtpurr", this.cookieExpiryDays = 28
    }
    v.prototype.init = function() {
        null == function(e) {
            for (var t = "".concat(e, "="), n = document.cookie.split(";"), o = 0, i = n.length; o < i; o += 1) {
                for (var r = n[o];
                    " " === r.charAt(0);) r = r.substring(1, r.length);
                if (0 === r.indexOf(t)) return decodeURIComponent(r.substring(t.length))
            }
            return null
        }(this.cookieName) && (this.$module.classList.add("hmrc-user-research-banner--show"), this.$closeLink.addEventListener("click", this.eventHandlers.noThanksClick.bind(this)))
    }, v.prototype.eventHandlers = {
        noThanksClick: function(e) {
            e.preventDefault(),
                function(e, t) {
                    var n = arguments.length > 2 && arguments[2] !== undefined ? arguments[2] : {},
                        o = "".concat(e, "=").concat(t, "; path=/");
                    if (n.days) {
                        var i = new Date;
                        i.setTime(i.getTime() + 24 * n.days * 60 * 60 * 1e3), o = "".concat(o, "; expires=").concat(i.toGMTString())
                    }
                    "https:" === window.location.protocol && (o += "; Secure"), document.cookie = o
                }(this.cookieName, "suppress_for_all_services", {
                    days: this.cookieExpiryDays
                }), this.$module.classList.remove("hmrc-user-research-banner--show")
        }
    };
    t((function(t, n) {
        (function(e) {
            "Window" in this || "undefined" == typeof WorkerGlobalScope && "function" != typeof importScripts && function(e) {
                e.constructor ? e.Window = e.constructor : (e.Window = e.constructor = new Function("return function Window() {}")()).prototype = this
            }(this)
        }).call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
            function(e) {
                "Document" in this || "undefined" == typeof WorkerGlobalScope && "function" != typeof importScripts && (this.HTMLDocument ? this.Document = this.HTMLDocument : (this.Document = this.HTMLDocument = document.constructor = new Function("return function Document() {}")(), this.Document.prototype = document))
            }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
            function(e) {
                "Element" in this && "HTMLElement" in this || function() {
                    if (!window.Element || window.HTMLElement) {
                        window.Element = window.HTMLElement = new Function("return function Element() {}")();
                        var e, t = document.appendChild(document.createElement("body")),
                            n = t.appendChild(document.createElement("iframe")).contentWindow.document,
                            o = Element.prototype = n.appendChild(n.createElement("*")),
                            i = {},
                            r = function(e, t) {
                                var n, o, s, a = e.childNodes || [],
                                    c = -1;
                                if (1 === e.nodeType && e.constructor !== Element)
                                    for (n in e.constructor = Element, i) o = i[n], e[n] = o;
                                for (; s = t && a[++c];) r(s, t);
                                return e
                            },
                            s = document.getElementsByTagName("*"),
                            a = document.createElement,
                            c = 100;
                        o.attachEvent("onpropertychange", (function(e) {
                            for (var t, n = e.propertyName, r = !i.hasOwnProperty(n), a = o[n], c = i[n], u = -1; t = s[++u];) 1 === t.nodeType && (r || t[n] === c) && (t[n] = a);
                            i[n] = a
                        })), o.constructor = Element, o.hasAttribute || (o.hasAttribute = function(e) {
                            return null !== this.getAttribute(e)
                        }), u() || (document.onreadystatechange = u, e = setInterval(u, 25)), document.createElement = function(e) {
                            var t = a(String(e).toLowerCase());
                            return r(t)
                        }, document.removeChild(t)
                    } else window.HTMLElement = window.Element;

                    function u() {
                        return c-- || clearTimeout(e), !(!document.body || document.body.prototype || !/(complete|interactive)/.test(document.readyState) || (r(document, !0), e && document.body.prototype && clearTimeout(e), !document.body.prototype))
                    }
                }()
            }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
            function(e) {
                var t, n, o;
                "defineProperty" in Object && function() {
                    try {
                        return Object.defineProperty({}, "test", {
                            value: 42
                        }), !0
                    } catch (e) {
                        return !1
                    }
                }() || (t = Object.defineProperty, n = Object.prototype.hasOwnProperty("__defineGetter__"), o = "A property cannot both have accessors and be writable or have a value", Object.defineProperty = function(e, i, r) {
                    if (t && (e === window || e === document || e === Element.prototype || e instanceof Element)) return t(e, i, r);
                    if (null === e || !(e instanceof Object || "object" == typeof e)) throw new TypeError("Object.defineProperty called on non-object");
                    if (!(r instanceof Object)) throw new TypeError("Property description must be an object");
                    var s = String(i),
                        a = "value" in r || "writable" in r,
                        c = "get" in r && typeof r.get,
                        u = "set" in r && typeof r.set;
                    if (c) {
                        if ("function" !== c) throw new TypeError("Getter must be a function");
                        if (!n) throw new TypeError("Getters & setters cannot be defined on this javascript engine");
                        if (a) throw new TypeError(o);
                        Object.__defineGetter__.call(e, s, r.get)
                    } else e[s] = r.value;
                    if (u) {
                        if ("function" !== u) throw new TypeError("Setter must be a function");
                        if (!n) throw new TypeError("Getters & setters cannot be defined on this javascript engine");
                        if (a) throw new TypeError(o);
                        Object.__defineSetter__.call(e, s, r.set)
                    }
                    return "value" in r && (e[s] = r.value), e
                })
            }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
            function(e) {
                (function(e) {
                    if (!("Event" in e)) return !1;
                    if ("function" == typeof e.Event) return !0;
                    try {
                        return new Event("click"), !0
                    } catch (t) {
                        return !1
                    }
                })(this) || function() {
                    var t = {
                        click: 1,
                        dblclick: 1,
                        keyup: 1,
                        keypress: 1,
                        keydown: 1,
                        mousedown: 1,
                        mouseup: 1,
                        mousemove: 1,
                        mouseover: 1,
                        mouseenter: 1,
                        mouseleave: 1,
                        mouseout: 1,
                        storage: 1,
                        storagecommit: 1,
                        textinput: 1
                    };
                    if ("undefined" != typeof document && "undefined" != typeof window) {
                        var n = window.Event && window.Event.prototype || null;
                        window.Event = Window.prototype.Event = function(t, n) {
                            if (!t) throw new Error("Not enough arguments");
                            var o;
                            if ("createEvent" in document) {
                                o = document.createEvent("Event");
                                var i = !(!n || n.bubbles === e) && n.bubbles,
                                    r = !(!n || n.cancelable === e) && n.cancelable;
                                return o.initEvent(t, i, r), o
                            }
                            return (o = document.createEventObject()).type = t, o.bubbles = !(!n || n.bubbles === e) && n.bubbles, o.cancelable = !(!n || n.cancelable === e) && n.cancelable, o
                        }, n && Object.defineProperty(window.Event, "prototype", {
                            configurable: !1,
                            enumerable: !1,
                            writable: !0,
                            value: n
                        }), "createEvent" in document || (window.addEventListener = Window.prototype.addEventListener = Document.prototype.addEventListener = Element.prototype.addEventListener = function() {
                            var e = this,
                                n = arguments[0],
                                i = arguments[1];
                            if (e === window && n in t) throw new Error("In IE8 the event: " + n + " is not available on the window object. Please see https://github.com/Financial-Times/polyfill-service/issues/317 for more information.");
                            e._events || (e._events = {}), e._events[n] || (e._events[n] = function(t) {
                                var n, i = e._events[t.type].list,
                                    r = i.slice(),
                                    s = -1,
                                    a = r.length;
                                for (t.preventDefault = function() {
                                        !1 !== t.cancelable && (t.returnValue = !1)
                                    }, t.stopPropagation = function() {
                                        t.cancelBubble = !0
                                    }, t.stopImmediatePropagation = function() {
                                        t.cancelBubble = !0, t.cancelImmediate = !0
                                    }, t.currentTarget = e, t.relatedTarget = t.fromElement || null, t.target = t.target || t.srcElement || e, t.timeStamp = (new Date).getTime(), t.clientX && (t.pageX = t.clientX + document.documentElement.scrollLeft, t.pageY = t.clientY + document.documentElement.scrollTop); ++s < a && !t.cancelImmediate;) s in r && -1 !== o(i, n = r[s]) && "function" == typeof n && n.call(e, t)
                            }, e._events[n].list = [], e.attachEvent && e.attachEvent("on" + n, e._events[n])), e._events[n].list.push(i)
                        }, window.removeEventListener = Window.prototype.removeEventListener = Document.prototype.removeEventListener = Element.prototype.removeEventListener = function() {
                            var e, t = this,
                                n = arguments[0],
                                i = arguments[1];
                            t._events && t._events[n] && t._events[n].list && -1 !== (e = o(t._events[n].list, i)) && (t._events[n].list.splice(e, 1), t._events[n].list.length || (t.detachEvent && t.detachEvent("on" + n, t._events[n]), delete t._events[n]))
                        }, window.dispatchEvent = Window.prototype.dispatchEvent = Document.prototype.dispatchEvent = Element.prototype.dispatchEvent = function(e) {
                            if (!arguments.length) throw new Error("Not enough arguments");
                            if (!e || "string" != typeof e.type) throw new Error("DOM Events Exception 0");
                            var t = this,
                                n = e.type;
                            try {
                                if (!e.bubbles) {
                                    e.cancelBubble = !0;
                                    var o = function(e) {
                                        e.cancelBubble = !0, (t || window).detachEvent("on" + n, o)
                                    };
                                    this.attachEvent("on" + n, o)
                                }
                                this.fireEvent("on" + n, e)
                            } catch (i) {
                                e.target = t;
                                do {
                                    e.currentTarget = t, "_events" in t && "function" == typeof t._events[n] && t._events[n].call(t, e), "function" == typeof t["on" + n] && t["on" + n].call(t, e), t = 9 === t.nodeType ? t.parentWindow : t.parentNode
                                } while (t && !e.cancelBubble)
                            }
                            return !0
                        }, document.attachEvent("onreadystatechange", (function() {
                            "complete" === document.readyState && document.dispatchEvent(new Event("DOMContentLoaded", {
                                bubbles: !0
                            }))
                        })))
                    }

                    function o(e, t) {
                        for (var n = -1, o = e.length; ++n < o;)
                            if (n in e && e[n] === t) return n;
                        return -1
                    }
                }()
            }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {})
    })), t((function(t, n) {
        (function(e) {
            var t, n, o;
            "defineProperty" in Object && function() {
                try {
                    return Object.defineProperty({}, "test", {
                        value: 42
                    }), !0
                } catch (e) {
                    return !1
                }
            }() || (t = Object.defineProperty, n = Object.prototype.hasOwnProperty("__defineGetter__"), o = "A property cannot both have accessors and be writable or have a value", Object.defineProperty = function(e, i, r) {
                if (t && (e === window || e === document || e === Element.prototype || e instanceof Element)) return t(e, i, r);
                if (null === e || !(e instanceof Object || "object" == typeof e)) throw new TypeError("Object.defineProperty called on non-object");
                if (!(r instanceof Object)) throw new TypeError("Property description must be an object");
                var s = String(i),
                    a = "value" in r || "writable" in r,
                    c = "get" in r && typeof r.get,
                    u = "set" in r && typeof r.set;
                if (c) {
                    if ("function" !== c) throw new TypeError("Getter must be a function");
                    if (!n) throw new TypeError("Getters & setters cannot be defined on this javascript engine");
                    if (a) throw new TypeError(o);
                    Object.__defineGetter__.call(e, s, r.get)
                } else e[s] = r.value;
                if (u) {
                    if ("function" !== u) throw new TypeError("Setter must be a function");
                    if (!n) throw new TypeError("Getters & setters cannot be defined on this javascript engine");
                    if (a) throw new TypeError(o);
                    Object.__defineSetter__.call(e, s, r.set)
                }
                return "value" in r && (e[s] = r.value), e
            })
        }).call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
            function(e) {
                var t;
                "DOMTokenList" in this && (!("classList" in (t = document.createElement("x"))) || !t.classList.toggle("x", !1) && !t.className) || function(t) {
                    var n;
                    "DOMTokenList" in t && t.DOMTokenList && (!document.createElementNS || !document.createElementNS("http://www.w3.org/2000/svg", "svg") || document.createElementNS("http://www.w3.org/2000/svg", "svg").classList instanceof DOMTokenList) || (t.DOMTokenList = function() {
                            var t = !0,
                                o = function(e, n, o, i) {
                                    Object.defineProperty ? Object.defineProperty(e, n, {
                                        configurable: !1 === t || !!i,
                                        get: o
                                    }) : e.__defineGetter__(n, o)
                                };
                            try {
                                o({}, "support")
                            } catch (n) {
                                t = !1
                            }
                            return function(t, n) {
                                var i = this,
                                    r = [],
                                    s = {},
                                    a = 0,
                                    c = 0,
                                    u = function(e) {
                                        o(i, e, (function() {
                                            return d(), r[e]
                                        }), !1)
                                    },
                                    l = function() {
                                        if (a >= c)
                                            for (; c < a; ++c) u(c)
                                    },
                                    d = function() {
                                        var e, o, i = arguments,
                                            c = /\s+/;
                                        if (i.length)
                                            for (o = 0; o < i.length; ++o)
                                                if (c.test(i[o])) throw (e = new SyntaxError('String "' + i[o] + '" contains an invalid character')).code = 5, e.name = "InvalidCharacterError", e;
                                        for ("" === (r = "object" == typeof t[n] ? ("" + t[n].baseVal).replace(/^\s+|\s+$/g, "").split(c) : ("" + t[n]).replace(/^\s+|\s+$/g, "").split(c))[0] && (r = []), s = {}, o = 0; o < r.length; ++o) s[r[o]] = !0;
                                        a = r.length, l()
                                    };
                                return d(), o(i, "length", (function() {
                                    return d(), a
                                })), i.toLocaleString = i.toString = function() {
                                    return d(), r.join(" ")
                                }, i.item = function(e) {
                                    return d(), r[e]
                                }, i.contains = function(e) {
                                    return d(), !!s[e]
                                }, i.add = function() {
                                    d.apply(i, e = arguments);
                                    for (var e, o, c = 0, u = e.length; c < u; ++c) o = e[c], s[o] || (r.push(o), s[o] = !0);
                                    a !== r.length && (a = r.length >>> 0, "object" == typeof t[n] ? t[n].baseVal = r.join(" ") : t[n] = r.join(" "), l())
                                }, i.remove = function() {
                                    d.apply(i, e = arguments);
                                    for (var e, o = {}, c = 0, u = []; c < e.length; ++c) o[e[c]] = !0, delete s[e[c]];
                                    for (c = 0; c < r.length; ++c) o[r[c]] || u.push(r[c]);
                                    r = u, a = u.length >>> 0, "object" == typeof t[n] ? t[n].baseVal = r.join(" ") : t[n] = r.join(" "), l()
                                }, i.toggle = function(t, n) {
                                    return d.apply(i, [t]), e !== n ? n ? (i.add(t), !0) : (i.remove(t), !1) : s[t] ? (i.remove(t), !1) : (i.add(t), !0)
                                }, i
                            }
                        }()), "classList" in (n = document.createElement("span")) && (n.classList.toggle("x", !1), n.classList.contains("x") && (n.classList.constructor.prototype.toggle = function(t) {
                            var n = arguments[1];
                            if (n === e) {
                                var o = !this.contains(t);
                                return this[o ? "add" : "remove"](t), o
                            }
                            return this[(n = !!n) ? "add" : "remove"](t), n
                        })),
                        function() {
                            var e = document.createElement("span");
                            if ("classList" in e && (e.classList.add("a", "b"), !e.classList.contains("b"))) {
                                var t = e.classList.constructor.prototype.add;
                                e.classList.constructor.prototype.add = function() {
                                    for (var e = arguments, n = arguments.length, o = 0; o < n; o++) t.call(this, e[o])
                                }
                            }
                        }(),
                        function() {
                            var e = document.createElement("span");
                            if ("classList" in e && (e.classList.add("a"), e.classList.add("b"), e.classList.remove("a", "b"), e.classList.contains("b"))) {
                                var t = e.classList.constructor.prototype.remove;
                                e.classList.constructor.prototype.remove = function() {
                                    for (var e = arguments, n = arguments.length, o = 0; o < n; o++) t.call(this, e[o])
                                }
                            }
                        }()
                }(this)
            }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
            function(e) {
                "Document" in this || "undefined" == typeof WorkerGlobalScope && "function" != typeof importScripts && (this.HTMLDocument ? this.Document = this.HTMLDocument : (this.Document = this.HTMLDocument = document.constructor = new Function("return function Document() {}")(), this.Document.prototype = document))
            }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
            function(e) {
                "Element" in this && "HTMLElement" in this || function() {
                    if (!window.Element || window.HTMLElement) {
                        window.Element = window.HTMLElement = new Function("return function Element() {}")();
                        var e, t = document.appendChild(document.createElement("body")),
                            n = t.appendChild(document.createElement("iframe")).contentWindow.document,
                            o = Element.prototype = n.appendChild(n.createElement("*")),
                            i = {},
                            r = function(e, t) {
                                var n, o, s, a = e.childNodes || [],
                                    c = -1;
                                if (1 === e.nodeType && e.constructor !== Element)
                                    for (n in e.constructor = Element, i) o = i[n], e[n] = o;
                                for (; s = t && a[++c];) r(s, t);
                                return e
                            },
                            s = document.getElementsByTagName("*"),
                            a = document.createElement,
                            c = 100;
                        o.attachEvent("onpropertychange", (function(e) {
                            for (var t, n = e.propertyName, r = !i.hasOwnProperty(n), a = o[n], c = i[n], u = -1; t = s[++u];) 1 === t.nodeType && (r || t[n] === c) && (t[n] = a);
                            i[n] = a
                        })), o.constructor = Element, o.hasAttribute || (o.hasAttribute = function(e) {
                            return null !== this.getAttribute(e)
                        }), u() || (document.onreadystatechange = u, e = setInterval(u, 25)), document.createElement = function(e) {
                            var t = a(String(e).toLowerCase());
                            return r(t)
                        }, document.removeChild(t)
                    } else window.HTMLElement = window.Element;

                    function u() {
                        return c-- || clearTimeout(e), !(!document.body || document.body.prototype || !/(complete|interactive)/.test(document.readyState) || (r(document, !0), e && document.body.prototype && clearTimeout(e), !document.body.prototype))
                    }
                }()
            }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {}),
            function(e) {
                var t;
                "document" in this && "classList" in document.documentElement && "Element" in this && "classList" in Element.prototype && ((t = document.createElement("span")).classList.add("a", "b"), t.classList.contains("b")) || function(e) {
                    var n = !0,
                        o = function(e, t, o, i) {
                            Object.defineProperty ? Object.defineProperty(e, t, {
                                configurable: !1 === n || !!i,
                                get: o
                            }) : e.__defineGetter__(t, o)
                        };
                    try {
                        o({}, "support")
                    } catch (t) {
                        n = !1
                    }
                    var i = function(e, t, r) {
                        o(e.prototype, t, (function() {
                            var e, s = "__defineGetter__DEFINE_PROPERTY" + t;
                            if (this[s]) return e;
                            if (this[s] = !0, !1 === n) {
                                for (var a, c = i.mirror || document.createElement("div"), u = c.childNodes, l = u.length, d = 0; d < l; ++d)
                                    if (u[d]._R === this) {
                                        a = u[d];
                                        break
                                    } a || (a = c.appendChild(document.createElement("div"))), e = DOMTokenList.call(a, this, r)
                            } else e = new DOMTokenList(this, r);
                            return o(this, t, (function() {
                                return e
                            })), delete this[s], e
                        }), !0)
                    };
                    i(e.Element, "classList", "className"), i(e.HTMLElement, "classList", "className"), i(e.HTMLLinkElement, "relList", "rel"), i(e.HTMLAnchorElement, "relList", "rel"), i(e.HTMLAreaElement, "relList", "rel")
                }(this)
            }.call("object" == typeof window && window || "object" == typeof self && self || "object" == typeof e && e || {})
    }));

    function y(e) {
        this.$module = e, this.$textarea = e.querySelector(".hmrc-js-character-count"), this.$textarea && (this.$countMessage = e.querySelector('[id="'.concat(this.$textarea.id, '-info"]')))
    }
    y.prototype.defaults = {
        characterCountAttribute: "data-maxlength",
        wordCountAttribute: "data-maxwords"
    }, y.prototype.init = function() {
        var e = this.$module,
            t = this.$textarea,
            n = this.$countMessage;
        if (t && n) {
            t.insertAdjacentElement("afterend", n), this.options = this.getDataset(e);
            var o = this.defaults.characterCountAttribute;
            this.options.maxwords && (o = this.defaults.wordCountAttribute), this.maxLength = e.getAttribute(o), this.maxLength && (e.removeAttribute("maxlength"), "onpageshow" in window ? window.addEventListener("pageshow", this.sync.bind(this)) : window.addEventListener("DOMContentLoaded", this.sync.bind(this)), this.sync())
        }
    }, y.prototype.sync = function() {
        this.bindChangeEvents(), this.updateCountMessage()
    }, y.prototype.getDataset = function(e) {
        var t = {},
            n = e.attributes;
        if (n)
            for (var o = 0; o < n.length; o++) {
                var i = n[o],
                    r = i.name.match(/^data-(.+)/);
                r && (t[r[1]] = i.value)
            }
        return t
    }, y.prototype.count = function(e) {
        var t;
        this.options.maxwords ? t = (e.match(/\S+/g) || []).length : t = e.length;
        return t
    }, y.prototype.bindChangeEvents = function() {
        var e = this.$textarea;
        e.addEventListener("keyup", this.checkIfValueChanged.bind(this)), e.addEventListener("focus", this.handleFocus.bind(this)), e.addEventListener("blur", this.handleBlur.bind(this))
    }, y.prototype.checkIfValueChanged = function() {
        this.$textarea.oldValue || (this.$textarea.oldValue = ""), this.$textarea.value !== this.$textarea.oldValue && (this.$textarea.oldValue = this.$textarea.value, this.updateCountMessage())
    }, y.prototype.updateCountMessage = function() {
        var e = this.$textarea,
            t = this.options,
            n = this.$countMessage,
            o = this.count(e.value),
            i = this.maxLength,
            r = i - o;
        i * (t.threshold ? t.threshold : 0) / 100 > o ? (n.classList.add("hmrc-character-count__message--disabled"), n.setAttribute("aria-hidden", !0)) : (n.classList.remove("hmrc-character-count__message--disabled"), n.removeAttribute("aria-hidden")), r < 0 ? (e.classList.add("govuk-textarea--error"), n.classList.remove("govuk-hint"), n.classList.add("govuk-error-message")) : (e.classList.remove("govuk-textarea--error"), n.classList.remove("govuk-error-message"), n.classList.add("govuk-hint"));
        var s, a, c = "cy" === this.options.language,
            u = c ? "Mae gennych " : "You have ";
        a = t.maxwords ? -1 === r || 1 === r ? c ? "gair" : "word" : c ? "o eiriau" : "words" : -1 === r || 1 === r ? c ? "cymeriad" : "character" : c ? "o gymeriadau" : "characters", s = r < 0 ? c ? "yn ormod" : "too many" : c ? "yn weddill" : "remaining";
        var l = Math.abs(r);
        n.innerHTML = u + l + " " + a + " " + s
    }, y.prototype.handleFocus = function() {
        this.valueChecker = setInterval(this.checkIfValueChanged.bind(this), 1e3)
    }, y.prototype.handleBlur = function() {
        clearInterval(this.valueChecker)
    };
    var w = {
        initAll: function() {
            document.querySelector('[data-module="hmrc-account-menu"]') && new u('[data-module="hmrc-account-menu"]').init();
            var e = document.querySelector('meta[name="hmrc-timeout-dialog"]');
            e && new b(e).init();
            var t = document.querySelector('[data-module="hmrc-user-research-banner"]');
            t && new v(t).init(), l(document.querySelectorAll('[data-module="hmrc-character-count"]'), (function(e) {
                new y(e).init()
            }))
        },
        AccountMenu: u,
        TimeoutDialog: b,
        UserResearchBanner: v,
        CharacterCount: y
    };
    window.GOVUKFrontend = n, window.HMRCFrontend = w, n.initAll(), w.initAll()
}();

//# sourceMappingURL=maps/hmrc-frontend-2.0.0.min.js.map