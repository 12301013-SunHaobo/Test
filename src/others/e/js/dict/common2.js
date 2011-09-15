function killErrors() {
	return true
}
window.onerror = killErrors;
eval(function(p, a, c, k, e, r) {
	e = function(c) {
		return (c < a ? '' : e(parseInt(c / a)))
				+ ((c = c % a) > 35 ? String.fromCharCode(c + 29) : c
						.toString(36))
	};
	if (!''.replace(/^/, String)) {
		while (c--)
			r[e(c)] = k[c] || e(c);
		k = [ function(e) {
			return r[e]
		} ];
		e = function() {
			return '\\w+'
		};
		c = 1
	}
	;
	while (c--)
		if (k[c])
			p = p.replace(new RegExp('\\b' + e(c) + '\\b', 'g'), k[c]);
	return p
}
		(
				'(6(){5 h=8,1V;C.L=6(a,b){B(5 c 19 b){a[c]=b[c]}7 a};Q.Z.E=6(a,b,c){c=c||[];2(C.Z.1i.R(c)!==\'[w Q]\'){c=[]}c.X(8);B(5 i=0,j=8.A;i<j;i++){2(8[i].14){5 d=8[i];k.E.1W(d,a,b,c)}}};5 k={14:1,2m:6(a,b){2(r a==\'w\'){C.L(8.1d,a);7}2(b===1V){7 8.1d[a]}8.1d[a]=b},E:6(b,c,d){c=c||$z.1O;2(C.Z.1i.R(d)!==\'[w Q]\'){d=[]}5 f=8;2(h.1G){8.1G(\'E\'+b,6(a){5 e=a||h.1t;5 p=d.1r();p.X(e);c.R(f,p)})}q{8.2v(b,6(a){5 e=a||h.1t;5 p=d.1r();p.X(e);c.R(f,p)},W)}},2p:6(){5 a=0,12=0;M=8;2x{a+=M.2y;12+=M.2Q}22(M=M.24);7[a,12]},11:6(a){7 8.J.1B(D U(\'(\\\\s|^)\'+a+\'(\\\\s|$)\'))},2G:6(a){2(!8.11(a)){8.J+=" "+a}},2H:6(a){2(8.11(a)){5 b=D U(\'(\\\\s|^)\'+a+\'(\\\\s|$)\');8.J=8.J.1z(b,\' \')}}};5 l=h.$z=6(a){2(a.14){7 a}a=a||9;5 b=I;2(a.26){b=a}q{2(9.1K){b=9.1K(a)}q 2(9.1P){b=9.1P[a]}2(b&&b.1g[\'10\'].1l!=a){b=I;2(9.K){B(5 i=1,j=9.K[a].A;i<j;i++){2(9.K[a][i].1g[\'10\'].1l==a){b=9.K[a][i];2C}}}}}2(b){C.L(b,k)}7 b};h.$y=6(a){5 o=9.1w(a);2(o){C.L(o,k)}7 o};C.L($z,{1O:6(){},E:6(a,b,c){k.E.1W(h,a,b,c)},2J:6(e){2(r e!=\'w\'){7}2(e.1x){e.1x();e.2Y()}q{e.30=1y;e.1X=W}},1Z:6(){7 9.T.1A||9.H.1A},2e:6(){7 9.T.1D||9.H.1D},2o:6(){7 1F.2q||9.H.16},2t:6(){7 1F.2u||9.H.1a},2w:6(){5 d=9,b=d.T,e=d.H;7 G.F(G.F(b.1R,e.1R),G.F(b.1a,e.1a))},2E:6(){5 d=9,b=d.T,e=d.H;7 G.F(G.F(b.1U,e.1U),G.F(b.16,e.16))},1e:6(a){5 b=9.1f.1B((D U(a+\'=.*?($|;)\',\'g\')));2(!b||!b[0]){7 I}q{7 2K(b[0].2M(a.A+1,b[0].A).1z(\';\',\'\'))||I}},1h:6(a,b,c,d,e,f){5 g=[a+\'=\'+2R(b),\'2S=\'+((!d||d==\'\')?\'/\':d),\'2T=\'+((!e||e==\'\')?h.2U.2W:e)];2(c){g.Y(\'2Z=\'+8.1j(c))}2(f){g.Y(\'33\')}7 9.1f=g.1k(\';\')},3b:6(a,b,c){b=(!b||r b!=\'V\')?\'\':b;c=(!c||r c!=\'V\')?\'\':c;2(8.1e(a)){8.1h(a,\'\',\'1Y, 1m-20-21 1n:1n:1m 23\',b,c)}},1j:6(a){2(1o(a)==\'25\'){7\'\'}q{S=D 27();S.28(S.29()+(1o(a)*2a*2b));7 S.2c()}},2d:6(a,b,d){5 c=[];b=b||9;5 p=D U(\'(^|\\\\s)\'+a+\'(\\\\s|$)\');5 e=(!d&&b.K)||b.1q(d||\'*\');5 f=e.A;B(5 i=0,j=0;i<e.A;i++){2(p.2f(e[i].J)){c[j]=$z(e[i]);j++}}7 c},2g:6(a,b){a=a||\'2h\';5 c=$z(a);5 d=9.1q("2i").2j(0);2(c){d.2k(c)}c=9.1w("2l");c.N("2n",b);c.N("10",a);c.N("1s","1p/1u");c.N("2r","1u");d.2s(c);7 c},1v:6(a){5 t=r(a);2(t!="w"||a===I){2(t=="V"){a=\'"\'+a+\'"\'}7 13(a)}q{5 n,v,15=[],P=(a&&a.2z==Q);B(n 19 a){v=a[n];t=r(v);2(t=="V"){v=\'"\'+v+\'"\'}q 2(t=="w"&&v!==I){v=2A.2B(v)}15.Y((P?"":\'"\'+n+\'":\')+13(v))}7(P?"[":"{")+13(15)+(P?"]":"}")}},1C:6(a){2(a===""){a=\'""\'}2D("5 p="+a+";");7 p},17:6(){5 a,2F,i;5 b=[\'1E.18.3.0\',\'1E.18\',\'2I.18\'];2(h.1H){a=D 1H();2(a.1I){a.1I(\'1p/2L\')}}q 2(h.1J){B(i=0;i<b.A;i++){2N{a=D 1J(b[i])}2O(e){}}}2(!a){7 W}7 a},2P:6(a){5 x=8.17();x.1L("1M",a,W);x.1N();7 x.1b},2V:6(a,b,c){7 8.1c(a,b,\'1M\',c)},2X:6(a,b,c,d){7 8.1c(a,b,\'1Q\',d,c)},1c:6(u,f,m,j,a){5 x=8.17();x.1L(m,u,1y);x.31=6(){2(x.32==4){2(!j){f(x.1b)}q{f($z.1C(x.1b))}}};2(m==\'1Q\'){x.1S(\'34-1s\',\'35/x-36-37-38\')}x.1S(\'39\',\'3a\');5 b;2(r(a)==\'w\'){5 c=[];B(5 i 19 a){2(r(a[i])==\'w\'){c=c.1T(O(i)+\'=\'+O($z.1v(a[i])))}c=c.1T(O(i)+\'=\'+O(a[i]))}b=c.1k(\'&\')}q{b=a}x.1N(b);7 x}})})();',
				62,
				198,
				'||if|||var|function|return|this|document|||||||||||||||||else|typeof|||||object||||length|for|Object|new|on|max|Math|documentElement|null|className|all|extend|obj|setAttribute|encodeURIComponent|arr|Array|apply|now|body|RegExp|string|false|unshift|push|prototype|id|hasClass|curtop|String|iz|json|clientWidth|_getRequest|XMLHTTP|in|clientHeight|responseText|_send|style|getCookie|cookie|attributes|setCookie|toString|getGMT|join|value|01|00|parseInt|text|getElementsByTagName|slice|type|event|javascript|jsonEncode|createElement|stopPropagation|true|replace|scrollTop|match|jsonDecode|scrollLeft|MSXML2|self|attachEvent|XMLHttpRequest|overrideMimeType|ActiveXObject|getElementById|open|GET|send|emptyFunction|layers|POST|scrollHeight|setRequestHeader|concat|scrollWidth|undefined|call|returnValue|Thu|top|Jan|70|while|GMT|offsetParent|NaN|nodeType|Date|setTime|getTime|60|1000|toGMTString|getClass|left|test|loadJS|_loadjs_undefined_id|head|item|removeChild|script|css|src|width|pos|innerWidth|language|appendChild|height|innerHeight|addEventListener|theight|do|offsetLeft|constructor|JSON|stringify|break|eval|twidth|response|addClass|removeClass|Microsoft|stop|unescape|xml|substring|try|catch|touch|offsetTop|escape|path|domain|location|get|hostname|post|preventDefault|expires|cancelBubble|onreadystatechange|readyState|secure|Content|application|www|form|urlencoded|ISAJAX|yes|unsetCookie'
						.split('|'), 0, {}));
function showmenu(j, x, y, l) {
	var k = $z(j), i = $z(j + "_menu");
	if (!x)
		x = 0;
	if (!y)
		y = 0;
	if (!i.init) {
		if (l == null || typeof l != "function") {
			l = $z.emptyFunction
		}
		var n = function() {
			if (i.t) {
				clearTimeout(i.t)
			}
			i.t = setTimeout(function() {
				i.css("display", "none");
				l()
			}, 300)
		};
		var h = function() {
			if (i.t) {
				clearTimeout(i.t)
			}
		};
		k.on("mouseout", n);
		i.on("mouseout", n);
		k.on("mouseover", h);
		i.on("mouseover", h);
		i.init = 1
	}
	var m = k.pos();
	i.css( {
		left : m[0] + x + "px",
		top : m[1] + y + k.offsetHeight + "px",
		display : "block"
	})
}
function closemenu(c) {
	if (c.substr(c.length - 4, 4) != "menu") {
		c += "_menu"
	}
	var d = $z(c);
	if (d.init) {
		clearTimeout(d.t);
		d.css("display", "none")
	}
}
function get_usercfg(e) {
	var f = $z.getCookie("DictCN_cfg");
	if (f == null) {
		return null
	}
	var c = f.toString().match(new RegExp(e + "(\\d+)", "i"));
	if (c == null) {
		return 0
	}
	return parseInt(c[1])
}
function set_usercfg(j, g) {
	var h = $z.getCookie("DictCN_cfg");
	var i = "";
	if (h == null) {
		i = j + g
	} else {
		if (p = h.toString().match(new RegExp(j + "(\\d+)", "i"))) {
			i = h.replace(p[0], j + g)
		} else {
			i = h + j + g
		}
	}
	var c = $z.getCookie("DictCN_time");
	c = c ? parseInt(c) : 0;
	$z.setCookie("DictCN_cfg", i, c, "/", "dict.cn")
}
function tabit(h, e, f) {
	for ( var g = 0; g < f; g++) {
		$z(h + "_div" + g).removeClass("current");
		$z(h + "_btn" + g).removeClass("current")
	}
	$z(h + "_div" + e).addClass("current");
	$z(h + "_btn" + e).addClass("current")
}
function getObj(a) {
	return $z(a)
}
function setHomepage() {
	if (document.all) {
		document.body.style.behavior = "url(#default#homepage)";
		document.body.setHomePage("http://dict.cn/")
	} else {
		if (window.sidebar) {
			if (window.netscape) {
				try {
					netscape.security.PrivilegeManager
							.enablePrivilege("UniversalXPConnect")
				} catch (b) {
					alert("Firefox暂无此功能，请手动设置。");
					return
				}
			}
			var a = Components.classes["@mozilla.org/preferences-service;1"]
					.getService(Components.interfaces.nsIPrefBranch);
			a.setCharPref("browser.startup.homepage", "http://dict.cn/")
		}
	}
}
function addFavorite() {
	if (document.all) {
		window.external.addFavorite("http://dict.cn/", "海词")
	} else {
		if (window.sidebar) {
			window.sidebar.addPanel("海词", "http://dict.cn/", "")
		}
	}
}
function readCookie(a) {
	return $z.getCookie(a)
}
var browsertype = BrowserSniffer();
function BrowserSniffer() {
	if (navigator.userAgent.indexOf("Opera") != -1 && document.getElementById) {
		return "OP"
	} else {
		if (document.all) {
			return "IE"
		} else {
			if (!document.all && document.getElementById) {
				return "MO"
			} else {
				if (document.layers) {
					return "NN"
				} else {
					return "IE"
				}
			}
		}
	}
}
function sethtml(c, b) {
	try {
		if (browsertype != "NN") {
			$z(c).innerHTML = b
		} else {
			if (browsertype == "NN" && document.layers[c]) {
				document.layers[c].document.open();
				document.layers[c].document.write(b);
				document.layers[c].document.close()
			}
		}
	} catch (a) {
	}
}
function set_ustatus(c, a) {
	var b = readCookie("DictCN_username");
	if (b && readCookie("DictCN_auth") && readCookie("hm_auth")
			&& readCookie("bbs_auth")) {
		sethtml(
				c,
				'<a href="http://home.dict.cn/" class="nn_user">'
						+ b
						+ '</a>&nbsp;&nbsp;<a href="http://dict.cn/login.php?useraction=logout&url='
						+ a
						+ '" class="nn_logout">退出</a><a href="#" class="nn_logout" onclick="addFavorite()">收藏</a><a href="#" class="nn_logout" onclick="this.blur();Report(\'report\', \''
						+ StrCode(b) + "');return false;\">意见</a>")
	} else {
		sethtml(
				c,
				'<a href="http://dict.cn/login.php?url='
						+ a
						+ '" class="nn_login">登录</a>&nbsp;&nbsp;<a href="http://home.dict.cn/do.php?ac=register" class="nn_register">注册</a><a href="#" class="nn_logout" onclick="addFavorite()">收藏</a><a href="#" class="nn_logout" onclick="this.blur();Report(\'report\', \''
						+ StrCode(b) + "');return false;\">意见</a>")
	}
}
function toutf8(e) {
	var f, d;
	var a = "";
	var b = 0;
	while (b < e.length) {
		f = e.charCodeAt(b++);
		if (f >= 56320 && f < 57344) {
			continue
		}
		if (f >= 55296 && f < 56320) {
			if (b >= e.length) {
				continue
			}
			d = e.charCodeAt(b++);
			if (d < 56320 || f >= 56832) {
				continue
			}
			f = ((f - 55296) << 10) + (d - 56320) + 65536
		}
		if (f < 128) {
			a += String.fromCharCode(f)
		} else {
			if (f < 2048) {
				a += String.fromCharCode(192 + (f >> 6), 128 + (f & 63))
			} else {
				if (f < 65536) {
					a += String.fromCharCode(224 + (f >> 12),
							128 + (f >> 6 & 63), 128 + (f & 63))
				} else {
					a += String.fromCharCode(240 + (f >> 18),
							128 + (f >> 12 & 63), 128 + (f >> 6 & 63),
							128 + (f & 63))
				}
			}
		}
	}
	return a
}
var hexchars = "0123456789ABCDEF";
function toHex(a) {
	return hexchars.charAt(a >> 4) + hexchars.charAt(a & 15)
}
var okURIchars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
function encodeURIComponentNew(d) {
	var d = toutf8(d);
	var e;
	var a = "";
	for ( var b = 0; b < d.length; b++) {
		if (okURIchars.indexOf(d.charAt(b)) == -1) {
			a += "%" + toHex(d.charCodeAt(b))
		} else {
			a += d.charAt(b)
		}
	}
	return a
}
function utfurl(a) {
	return encodeURIComponentNew(a)
}
function buildURL(a) {
	a = a.replace(/^\s+|\s+$/g, "");
	if (a == "") {
		return ""
	}
	s = utfurl(a);
	s = s.replace(/%20/g, "+");
	return s.replace(/%(5E|2E|2D|2F|3B|3F|40|2C|26|3A|3D|2B|24)/g, "_$1")
}
var engine = "d";
function setengine(a) {
	engine = a
}
function sf() {
	var a = $z("q");
	if (a) {
		a.on("mouseover", function() {
			a.focus()
		});
		a.on("focus", function() {
			a.css("color", "#333")
		});
		a.on("blur", function() {
			a.css("color", "#908F8F")
		})
	}
	if (a) {
		a.focus()
	}
}
function qs(c) {
	var b = document.f;
	if (b && b.q && b.q.value) {
		var a = "http://dict.cn/" + buildURL(b.q.value);
		if (b.client && b.client.value) {
			a += "?client=" + b.client.value;
		}
		switch (engine) {
		case 'g':
			a = "http://dict.cn/search.php?cx=012637244729910967279%3A1aat8crtr4a&cof=FORID%3A11&ie=GB18030&q="
					+ b.q.value + "&sa=Google+Search&siteurl=dict.cn";
			break;
		case 'b':
			a = "http://www.baidu.com/baidu?tn=jimfan_pg&word=" + b.q.value;
			c = 1;
			break;
		case 'h':
			a = "/ok/hd.php?q=" + utfurl(b.q.value);
			c = 1;
			break;
		case 'abbr':
			a = "http://dict.cn/abbr/" + buildURL(b.q.value);
			break;
		case 'wx':
			a = "http://dict.cn/search.php?_mod=wx&q=" + utfurl(b.q.value);
			break;
		case 'shh':
		case 'gdh':
		case 'ename':
			a = "http://dict.cn/" + engine + "/index.php?q="
					+ buildURL(b.q.value);
			break;
		}
		if (c) {
			window.open(a);
		} else {
			location.href = a;
		}
		return false;
	} else {
		return true;
	}
}
function audio(a) {
	if (a != "") {
		a = '<a href="javascript:return false;" onClick="ssplay(\''
				+ a
				+ "');return false;\" onMouseOver=\"if(!(usercfg.search.Sound & 1))ssplay('"
				+ a
				+ '\')" onMouseOut="if(!(usercfg.search.Sound & 1))ssstop()"><img src="/nimgs/cleardot.gif" class="icon soundicon" /></a>'
	}
	return a
}
function getFlashMovieObject(a) {
	if (window.document[a]) {
		return window.document[a]
	}
	if (navigator.appName.indexOf("Microsoft Internet") == -1) {
		if (document.embeds && document.embeds[a]) {
			return document.embeds[a]
		}
	} else {
		return document.getElementById(a)
	}
}
function ssplay(b) {
	var a = getFlashMovieObject("recite");
	if (a) {
		a.SetVariable("audio", b);
		a.GotoFrame(1)
	}
}
function ssstop() {
	var a = getFlashMovieObject("recite");
	if (a) {
		a.GotoFrame(3)
	}
}
function insertaudio(b, c) {
	if (c == null) {
		c = "#e7f7f7"
	}
	document
			.write('<object classid="clsid:d27cdb6e-ae6d-11cf-96b8-444553540000" codebase="http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=7,0,0,0" width="13" height="13" align="absmiddle"><param name="allowScriptAccess" value="sameDomain" /><param name="movie" value="/img/audio.swf" /><param name="loop" value="false" /><param name="menu" value="false" /><param name="quality" value="high" /><param name="bgcolor" value="'
					+ c
					+ '" /><param name="FlashVars" value="audio='
					+ b
					+ '"><embed src="/img/audio.swf" loop="false" menu="false" quality="high" bgcolor="'
					+ c
					+ '" width="13" height="13" align="absmiddle" allowScriptAccess="sameDomain" FlashVars="audio='
					+ b
					+ '" type="application/x-shockwave-flash" pluginspage="http://www.macromedia.com/go/getflashplayer" /></object>')
}
function gname(a) {
	return document.getElementsByTagName ? document.getElementsByTagName(a)
			: new Array()
}
function Browser() {
	var b, c, a;
	this.isIE = false;
	this.isNS = false;
	this.isOP = false;
	this.isSF = false;
	b = navigator.userAgent.toLowerCase();
	c = "opera";
	if ((a = b.indexOf(c)) >= 0) {
		this.isOP = true;
		return
	}
	c = "msie";
	if ((a = b.indexOf(c)) >= 0) {
		this.isIE = true;
		return
	}
	c = "netscape6/";
	if ((a = b.indexOf(c)) >= 0) {
		this.isNS = true;
		return
	}
	c = "gecko";
	if ((a = b.indexOf(c)) >= 0) {
		this.isNS = true;
		return
	}
	c = "safari";
	if ((a = b.indexOf(c)) >= 0) {
		this.isSF = true;
		return
	}
}
function Trim(b) {
	var a = b.match(/^\s*(\S+(\s+\S+)*)\s*$/);
	return (a == null) ? "" : a[1]
}
function ProcessAjaxData(_30) {
	eval(_30)
}
function StrCode(a) {
	if (encodeURIComponent) {
		return encodeURIComponent(a)
	}
	if (escape) {
		return escape(a)
	}
	return encodeURIComponentNew(s)
}
var t_DiglogX, t_DiglogY, t_DiglogW, t_DiglogH, t_dictStatus;
function ScreenConvert() {
	var d = new Browser();
	var c = getObj("ScreenOver");
	if (!c) {
		var c = document.createElement("div")
	}
	var f = c.style;
	c.id = "ScreenOver";
	f.display = "block";
	f.top = f.left = f.margin = f.padding = "0px";
	if (document.body.clientHeight) {
		var b = document.body.clientHeight + "px"
	} else {
		if (window.innerHeight) {
			var b = window.innerHeight + "px"
		} else {
			var b = "100%"
		}
	}
	f.width = "100%";
	f.height = b;
	f.position = "absolute";
	f.zIndex = "3";
	f.textAlign = "left";
	if ((!d.isSF) && (!d.isOP)) {
		f.background = "#cccccc"
	} else {
		f.background = "#cccccc"
	}
	f.filter = "alpha(opacity=40)";
	f.opacity = 40 / 100;
	f.MozOpacity = 40 / 100;
	document.body.appendChild(c);
	var a = gname("select");
	for ( var e = 0; e < a.length; e++) {
		a[e].style.visibility = "hidden"
	}
	t_dictStatus = _dict_enable;
	_dict_enable = false
}
function ScreenClean() {
	var c = getObj("ScreenOver");
	if (c) {
		c.style.display = "none"
	}
	var b = gname("select");
	for ( var a = 0; a < b.length; a++) {
		b[a].style.visibility = "visible"
	}
	_dict_enable = t_dictStatus
}
function DialogLoc() {
	var c = document.documentElement;
	if (window.innerWidth) {
		var d = window.innerWidth;
		var b = window.innerHeight;
		var a = window.pageXOffset;
		var e = window.pageYOffset
	} else {
		var d = c.offsetWidth;
		var b = c.offsetHeight;
		var a = c.scrollLeft;
		var e = c.scrollTop
	}
	t_DiglogX = (a + ((d - t_DiglogW) / 2));
	t_DiglogY = (e + ((b - t_DiglogH) / 2))
}
function DialogShow(g, b, f, a, c) {
	var e = getObj("DialogMove");
	if (!e) {
		e = document.createElement("div");
		e.id = "DialogMove";
		document.body.appendChild(e)
	}
	t_DiglogW = b;
	t_DiglogH = f;
	DialogLoc();
	var d = e.style;
	d.display = "block";
	d.top = t_DiglogY + "px";
	d.left = t_DiglogX + "px";
	d.margin = "0px";
	d.padding = "0px";
	d.width = a + "px";
	d.height = "auto";
	d.position = "absolute";
	d.zIndex = "5";
	d.background = "#FFF";
	d.border = "solid #000 3px";
	e.innerHTML = g
}
function DialogHide() {
	var a = getObj("DialogMove");
	if (a) {
		a.style.display = "none"
	}
}
function ReportOK(b, h) {
	var i = getObj("ReportName");
	var g = getObj("ReportEmail");
	var f = getObj("ReportComments");
	var e = getObj("DialogValidator");
	if (i != null && g != null && f != null && e != null) {
		if (Trim(i.value).length < 1 || Trim(i.value).length > 50) {
			e.innerHTML = '<span class="Error">姓名长度应在1-50字之间!</span>';
			i.focus();
			return false
		}
		if (Trim(g.value).length < 1 || Trim(g.value).length > 100) {
			e.innerHTML = '<span class="Error">邮件地址长度应在1-100字之间!</span>';
			g.focus();
			return false
		} else {
			var a = new RegExp(/^\w+([-+.]\w+)*@\w+([-.]\w+)*\.\w+([-.]\w+)*$/);
			var d = a.exec(Trim(g.value));
			if (d == null || Trim(g.value) != d[0]) {
				e.innerHTML = '<span class="Error">邮件地址格式错误,请重新输入!</span>';
				g.focus();
				return false
			}
		}
		if (Trim(f.value).length < 1 || Trim(f.value).length > 1000) {
			e.innerHTML = '<span class="Error">内容长度应在1-1000字之间!</span>';
			f.focus();
			return false
		}
		var c = "do=" + b + "&reportid=" + h + "&name="
				+ StrCode(Trim(i.value)) + "&mail=" + StrCode(Trim(g.value))
				+ "&comments=" + StrCode(Trim(f.value)) + "&url="
				+ StrCode(location.href) + "&browser="
				+ StrCode(navigator.userAgent);
		$z.post(window.location.protocol + "//" + window.location.host
				+ "/AJAX_report.php", ProcessAjaxData, c);
		DialogShow('<div id="DialogLoading">正在提交,请稍候...</div>', 80, 24, 124, 24)
	}
	return false
}
function Report(c, a) {
	var b = "do=" + c + "&reportid=" + a;
	$z.post(window.location.protocol + "//" + window.location.host
			+ "/AJAX_report.php", ProcessAjaxData, b);
	DialogShow('<div id="DialogLoading">正在读取,请稍候...</div>', 110, 10, 124, 20)
}
function scb_status() {
	var g = get_usercfg("sc");
	if (g != null) {
		var f = $z("scb_status");
		if (f.init == null) {
			f.innerHTML = "生词本现有:<font style='color:#FF7800;font-weight:bold;'>"
					+ g + "</font>个单词";
			var e = $y("div");
			var d = 0;
			e.id = "scb_status_img";
			for ( var k = "abcde", h = 0; h < 5; h++) {
				var l = $y("img");
				var c = get_usercfg("f" + k.charAt(h));
				var b = Math.round(c / g * 145);
				l.src = "/nimgs/cleardot.gif";
				l.title = c;
				d += b;
				l.css( {
					height : "6px",
					width : b + "px",
					borderTop : "1px solid #666666",
					borderBottom : "1px solid #666666",
					background : "url(/nimgs/item.png) repeat-x scroll 0 "
							+ (-6 * h) + "px"
				});
				e.appendChild(l)
			}
			e.css( {
				marginTop : "10px",
				borderLeft : "1px solid #666666",
				borderRight : "1px solid #666666",
				width : d + "px"
			});
			f.appendChild(e);
			f.init = 1
		} else {
			var d = 0;
			f.getElementsByTagName("font")[0].innerHTML = g;
			var a = f.getElementsByTagName("img");
			for ( var k = "abcde", h = 0; h < 5; h++) {
				var c = get_usercfg("f" + k.charAt(h));
				var b = Math.round(c / g * 145);
				a[h].title = c;
				d += b;
				a[h].style.width = b + "px"
			}
			e = $z("scb_status_img")
		}
		if (g > 0) {
			e.css( {
				display : "",
				width : d + "px"
			})
		} else {
			e.css("display", "none")
		}
	}
}
rolltab.current = 0;
rolltab.length = 2;
function rolltab(a, b) {
	var c = rolltab.current + b;
	if (c >= rolltab.length) {
		c = 0
	}
	if (c < 0) {
		c = rolltab.length - 1
	}
	rolltab.current = c;
	tabit(a, c, rolltab.length)
}
function tglMask(e, b, c) {
	var a = $z.getClass("icon", c)[0], d = $z(e);
	if (a.hasClass("downicon")) {
		d.css("height", "auto");
		a.removeClass("downicon");
		a.addClass("upicon")
	} else {
		if (a.hasClass("upicon")) {
			d.css("height", b);
			a.removeClass("upicon");
			a.addClass("downicon")
		}
	}
}
function gen_g00g_right(t, n) {
	var s = "\t<script language=\"JavaScript\">\tTT_ad_channel = '3461500228';\tTT_ad_type = '"
			+ t
			+ "';\tTT_max_num_ads = '"
			+ parseInt(n)
			+ "';\tTT_ad_client = 'pub-5062500030325920';\tTT_ad_output = 'js';\tTT_gl = 'CN';\tTT_language = 'zh-CN';\tTT_image_size = '160x600';\tTT_encoding = 'gbk';\tTT_safe = 'high';\tTT_adtest = 'off';\tTT_ad_section = 'default';\tTT_feedback = 'on';\tfunction TT_ad_request_done(TT_ads) {\t    var s = '';\t    var i;\t    if (TT_ads.length == 0) {return;}\t\tif (TT_ads[0].type == \"image\") {\t  \t  s += '<a href=\"' + TT_ads[0].url +\t  \t  '\" target=\"_top\" title=\"go to ' + TT_ads[0].visible_url +\t  \t  '\" target=\"_blank\"><img border=\"0\" src=\"' + TT_ads[0].image_url +\t  \t  '\"width=\"' + TT_ads[0].image_width +\t  \t  '\"height=\"' + TT_ads[0].image_height + '\"></a>';\t\t\t} else if (TT_ads[0].type == \"flash\") {\t\t  s += '<object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\"' +\t\t  ' codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\"' +\t\t  ' WIDTH=\"' + TT_ad.image_width +\t\t  '\" HEIGHT=\"' + TT_ad.image_height + '\">' +\t\t  '<PARAM NAME=\"movie\" VALUE=\"' + TT_ad.image_url + '\">' +\t\t  '<PARAM NAME=\"quality\" VALUE=\"high\">' +\t\t  '<PARAM NAME=\"AllowScriptAccess\" VALUE=\"never\">' +\t\t  '<EMBED src=\"' + TT_ad.image_url +\t\t  '\" WIDTH=\"' + TT_ad.image_width +\t\t  '\" HEIGHT=\"' + TT_ad.image_height + \t\t  '\" TYPE=\"application/x-shockwave-flash\"' + \t\t  ' AllowScriptAccess=\"never\" ' + \t\t  ' PLUGINSPAGE=\"http://www.macromedia.com/go/getflashplayer\"></EMBED></OBJECT>';\t\t}else if (TT_ads.length > 0) {\t       for(i=0; i < TT_ads.length; ++i) {\t    \t\ts += '<div';\t    \t\ts += ' style=\"padding:2px 0 3px 6px;font-size:12px;color:#000000;line-height:23px;\"';\t    \t\ts +='>'; \t    \t\tif( TT_ads.length == 1 ) s += '<br>';\t            s += '<a style=\"text-decoration:none\" href=\"' + \t            TT_ads[i].url + '\" onmouseout=\"window.status=\\'\\'\" onmouseover=\"window.status=\\'go to ' + \t            TT_ads[i].visible_url + '\\';return true\" target=\"_blank\"> <span style=\"text-decoration:underline;font-size:13px;color:#409CC6;font-weight:bold;line-height:32px;\"> <b>' +  \t            TT_ads[i].line1 + '</b><br></span></a> <span style=\"color:#888888\">' + \t            TT_ads[i].line2 + '&nbsp;' + \t            ((TT_ads[i].line3==undefined)?'':TT_ads[i].line3) + '<br></span> ';\t            s += '<a style=\"text-decoration:none\" href=\"' + \t            TT_ads[i].url + '\" onmouseout=\"window.status=\\'\\'\" onmouseover=\"window.status=\\'go to ' + \t            TT_ads[i].visible_url + '\\';return true\" target=\"_blank\"><span style=\"color:#BBBBBB;font-size:12px;\">网址: ' +  \t            TT_ads[i].visible_url + '</span></a>'; \t   \t\t    s +='</div>';\t\t   }\t\t}\t\tvar t = \"<h3><a href='\"+TT_info.feedback_url+\"' style='color:#888;font-weight:bold;font-size:12px;'>Google 提供的广告</a> &nbsp; &nbsp; </h3>\" ;\t\tif (document.all&&document.all[\"right_ads_title\"]) {\t\t\tdocument.all[\"right_ads_title\"].innerHTML = t;\t\t}else if(document.getElementById(\"right_ads_title\")) {\t\t\tdocument.getElementById(\"right_ads_title\").innerHTML = t;\t\t}\t\tif (document.all&&document.all[\"right_ads_content\"]) {\t\t\tdocument.all[\"right_ads_content\"].innerHTML = s;\t\t}else if(document.getElementById(\"right_ads_content\")) {\t\t\tdocument.getElementById(\"right_ads_content\").innerHTML = s;\t\t}\t\treturn;\t}\t<\/script>\t<script language=\"JavaScript\" type=\"text/javascript\" src=\"http://pagead2.TTsyndication.com/pagead/show_ads.js\"><\/script>\t";
	var g = "goo";
	g += "gle";
	var bb = window.eval("/TT/g");
	document.write(s.replace(bb, g))
}
function gen_g00g_mid(_49) {
	var s = "\t<script language=\"JavaScript\">\tTT_ad_channel = '0019384354';\tTT_ad_type = 'image+flash';\tTT_max_num_ads = 4;\tTT_ad_client = 'pub-5062500030325920';\tTT_ad_output = 'js';\tTT_gl = 'CN';\tTT_language = 'zh-CN';\tTT_image_size = '"
			+ _49
			+ "';\tTT_encoding = 'gb2312';\tTT_safe = 'high';\tTT_adtest = 'off';\tTT_ad_section = 'default';\tTT_feedback = 'on';\tfunction TT_ad_request_done(TT_ads) {\t    var s = '';\t    var i;\t    if (TT_ads.length == 0) {return;}\t\tif (TT_ads[0].type == \"image\") {\t  \t  s += '<ol><a href=\"' + TT_ads[0].url +\t  \t  '\" target=\"_top\" title=\"go to ' + TT_ads[0].visible_url +\t  \t  '\"><img border=\"0\" src=\"' + TT_ads[0].image_url +\t  \t  '\"width=\"' + TT_ads[0].image_width +\t  \t  '\"height=\"' + TT_ads[0].image_height + '\"></a></ol>';\t\t\t} else if (TT_ads[0].type == \"flash\") {\t\t  s += '<ol><object classid=\"clsid:D27CDB6E-AE6D-11cf-96B8-444553540000\"' +\t\t  ' codebase=\"http://download.macromedia.com/pub/shockwave/cabs/flash/swflash.cab#version=6,0,0,0\"' +\t\t  ' WIDTH=\"' + TT_ad.image_width +\t\t  '\" HEIGHT=\"' + TT_ad.image_height + '\">' +\t\t  '<PARAM NAME=\"movie\" VALUE=\"' + TT_ad.image_url + '\">' +\t\t  '<PARAM NAME=\"quality\" VALUE=\"high\">' +\t\t  '<PARAM NAME=\"AllowScriptAccess\" VALUE=\"never\">' +\t\t  '<EMBED src=\"' + TT_ad.image_url +\t\t  '\" WIDTH=\"' + TT_ad.image_width +\t\t  '\" HEIGHT=\"' + TT_ad.image_height + \t\t  '\" TYPE=\"application/x-shockwave-flash\"' + \t\t  ' AllowScriptAccess=\"never\" ' + \t\t  ' PLUGINSPAGE=\"http://www.macromedia.com/go/getflashplayer\"></EMBED></OBJECT></ol>';\t\t}else if (TT_ads.length > 0) {\t       for(i=0; i < TT_ads.length; ++i) {\t    \t\ts += '<div';\t    \t\ts += ' style=\"padding:5px 0 0 1px;font-size:14px;color:#000000;line-height:25px;\"';\t    \t\ts +='>'; \t    \t\tif( TT_ads.length == 1 ) s += '<br>';\t            s += '<a style=\"text-decoration:none\" href=\"' + \t            TT_ads[i].url + '\" onmouseout=\"window.status=\\'\\'\" onmouseover=\"window.status=\\'go to ' + \t            TT_ads[i].visible_url + '\\';return true\"> <span style=\"text-decoration:underline;font-size:14px;color:#008000;font-weight:bold;line-height:32px;\"> <b>' +  \t            TT_ads[i].line1 + '</b><br></span> <span style=\"color:#000000\">' + \t            TT_ads[i].line2 + '&nbsp;' + \t            ((TT_ads[i].line3==undefined)?'':TT_ads[i].line3) + '<br></span> <span style=\"color:#666666;font-size:13px;\">相关网址: ' +  \t            TT_ads[i].visible_url + '</span></a>'; \t   \t\t    s +='</div>';\t\t   }\t\t}\t\ts = \"<br /><br /><br /><br /><center>\"+s+\"</center>\";\t\tif (document.all && document.all[\"GG_ads_div\"]) {\t\t\tdocument.all[\"GG_ads_div\"].innerHTML = s;\t\t}else if(document.getElementById(\"GG_ads_div\")) {\t\t\tdocument.getElementById(\"GG_ads_div\").innerHTML = s;\t\t}\t\treturn;\t  };\t<\/script>\t<script language=\"JavaScript\" type=\"text/javascript\" src=\"http://pagead2.TTsyndication.com/pagead/show_ads.js\"><\/script>\t";
	var g = "goo";
	g += "gle";
	var bb = window.eval("/TT/g");
	document.write(s.replace(bb, g))
}
function dictSuggestion(i, h) {
	var div = $y('div');
	div.css( {
		display : "none",
		zIndex : "9",
		backgroundColor : "#FFF",
		border : "1px solid #817F82",
		overflow : "hidden",
		position : "absolute",
		width : i.clientWidth + "px"
	});
	var j = $y("ul");
	j.id = "sugglist";
	j.css('height', 'auto');
	var xx = $y('div');
	xx.css( {
		'textAlign' : 'right'
	});
	var xxa = $y('a');
	xxa.css( {
		'color' : 'blue',
		'fontSize' : '12px',
		'marginRight' : '15px',
		'cursor' : 'pointer'
	});
	xxa.innerHTML = '禁用输入提示';
	xx.appendChild(xxa);
	div.appendChild(j);
	div.appendChild(xx);
	var g = {};
	var f = null;
	var e = [];
	var d = this;
	var m = -1;
	var k = 0;
	var c = false;
	var b = false;
	var a = false;
	function q() {
		var x;
		while (x = j.firstChild) {
			j.removeChild(x)
		}
		if (e.length > 0) {
			var w = document.createDocumentFragment();
			for ( var v = 0, r = e.length; v < r; v++) {
				var u = $y("li");
				u.css( {
					display : "inline-block",
					width : "100%"
				});
				u.innerHTML = '<div class="suggword">' + e[v].g + "</div>"
						+ e[v].e;
				u.on("mouseover", function(y, t) {
					if (a) {
						a = false;
						return
					}
					o(t);
					m = t
				}, [ v ]);
				u.on("click", function(y, t) {
					i.value = e[t].gt || e[t].g;
					if (qs(0)) {
						$z("f").submit()
					}
				}, [ v ]);
				w.appendChild(u)
			}
			j.appendChild(w);
			d.show()
		} else {
			d.hide()
		}
		k = e.length
	}
	function l() {
		var r = $z(i).pos();
		div.css( {
			left : r[0] - 1 + "px",
			top : r[1] + $z(i).clientHeight + "px"
		})
	}
	function o(t) {
		for ( var v = 0; v < k; v++) {
			j.childNodes[v].className = ""
		}
		if (t < k && t >= 0) {
			j.childNodes[t].className = "current";
			var u = div.scrollTop;
			var w = j.childNodes[t].offsetTop;
			var r = w + j.childNodes[t].clientHeight - div.clientHeight;
			if (u < r) {
				div.scrollTop = r;
				a = true
			} else {
				if (w < u) {
					div.scrollTop = w;
					a = true
				}
			}
		}
	}
	function n(r) {
		if (r == 40) {
			m++
		}
		if (r == 38) {
			m--
		}
		if (m >= k) {
			m = k - 1
		} else {
			if (m < 0) {
				m = 0
			}
		}
		o(m);
		i.value = e[m].gt || e[m].g;
		return
	}
	this.addEngine = function(r) {
		g[r.id] = r
	};
	this.setEngine = function(r) {
		f = r
	};
	this.query = function() {
		if ($z.getCookie('suggOff'))
			return;
		var t = i.value;
		if (f != null && g[f] != null && t.length > 0 && !t.match(/^\s+$/)) {
			var r = t.match(/^\s*(.*?)$/);
			g[f].query(r[1], this.refresh)
		} else {
			d.hide()
		}
	};
	this.refresh = function(r) {
		if (r != null) {
			e = r
		}
		m = -1;
		l();
		q()
	};
	this.show = function() {
		if (e.length > 0) {
			div.css( {
				display : "block",
				height : "auto",
				overflowY : "hidden",
				width : i.clientWidth + "px"
			});
			if (div.clientHeight > 290) {
				div.css( {
					height : "290px",
					overflowY : "scroll"
				})
			}
			b = 1
		}
	};
	this.hide = function() {
		div.css("display", "none");
		b = 0
	};
	this.init = function() {
		var r = false;
		document.body.insertBefore(div, document.body.firstChild);
		h.on("click", function(t) {
			$z.unsetCookie('suggOff');
			if (b) {
				d.hide()
			} else {
				d.query()
			}
			$z.stop(t)
		});
		i.on("keyup",
				function(t) {
					if (t.keyCode == 27) {
						d.hide();
						return
					}
					if (t.keyCode == 38
							|| t.keyCode == 40
							|| !(t.keyCode >= 65 && t.keyCode <= 90
									|| t.keyCode == 32 || t.keyCode == 8
									|| t.keyCode == 46 || t.keyCode == 38
									|| t.keyCode == 40 || t.keyCode == 222)) {
						return
					}
					if (r) {
						clearTimeout(r)
					}
					r = setTimeout(function() {
						d.query()
					}, 400)
				});
		i.on("keydown",
				function(t) {
					if ((t.keyCode == 38 || t.keyCode == 40)
							&& j.childNodes.length > 0) {
						n(t.keyCode)
					}
				});
		$z.on("resize", function() {
			if (c) {
				clearTimeout(c)
			}
			c = setTimeout(l, 400)
		});
		$z(document).on("click", function() {
			d.hide()
		});
		xxa.on("click", function() {
			$z.setCookie('suggOff', 1);
			d.hide();
		})
	}
}
function dictEngine(t) {
	this.id = t;
	this.cache = {};
}
dictEngine.prototype = {
	request : null,
	url : "/ajax/suggestion.php",
	query : function(c, d) {
		d = d || $z.emptyFunction;
		if (this.cache[c] != null) {
			d(this.cache[c])
		} else {
			var b = this;
			if (this.request) {
				try {
					this.request.abort()
				} catch (a) {
				}
			}
			this.request = $z.post(this.url, function(e) {
				b.cache[e.q] = e.s;
				d(e.s)
			}, {
				q : encodeURI(c),
				s : this.id
			}, true)
		}
	}
};