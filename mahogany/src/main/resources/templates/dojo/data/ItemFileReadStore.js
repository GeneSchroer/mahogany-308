/*
	Copyright (c) 2004-2016, The JS Foundation All Rights Reserved.
	Available via Academic Free License >= 2.1 OR the modified BSD license.
	see: http://dojotoolkit.org/license for details
*/

//>>built
define("dojo/data/ItemFileReadStore","../_base/kernel ../_base/lang ../_base/declare ../_base/array ../_base/xhr ../Evented ./util/filter ./util/simpleFetch ../date/stamp".split(" "),function(m,n,q,u,r,v,t,w,x){q=q("dojo.data.ItemFileReadStore",[v],{constructor:function(a){this._arrayOfAllItems=[];this._arrayOfTopLevelItems=[];this._loadFinished=!1;this.url=this._ccUrl=this._jsonFileUrl=a.url;this._jsonData=a.data;this.data=null;this._datatypeMap=a.typeMap||{};this._datatypeMap.Date||(this._datatypeMap.Date=
{type:Date,deserialize:function(a){return x.fromISOString(a)}});this._features={"dojo.data.api.Read":!0,"dojo.data.api.Identity":!0};this._itemsByIdentity=null;this._storeRefPropName="_S";this._itemNumPropName="_0";this._rootItemPropName="_RI";this._reverseRefMap="_RRM";this._loadInProgress=!1;this._queuedFetches=[];void 0!==a.urlPreventCache&&(this.urlPreventCache=a.urlPreventCache?!0:!1);void 0!==a.hierarchical&&(this.hierarchical=a.hierarchical?!0:!1);a.clearOnClose&&(this.clearOnClose=!0);"failOk"in
a&&(this.failOk=a.failOk?!0:!1)},url:"",_ccUrl:"",data:null,typeMap:null,clearOnClose:!1,urlPreventCache:!1,failOk:!1,hierarchical:!0,_assertIsItem:function(a){if(!this.isItem(a))throw Error(this.declaredClass+": Invalid item argument.");},_assertIsAttribute:function(a){if("string"!==typeof a)throw Error(this.declaredClass+": Invalid attribute argument.");},getValue:function(a,b,e){a=this.getValues(a,b);return 0<a.length?a[0]:e},getValues:function(a,b){this._assertIsItem(a);this._assertIsAttribute(b);
return(a[b]||[]).slice(0)},getAttributes:function(a){this._assertIsItem(a);var b=[],e;for(e in a)e!==this._storeRefPropName&&e!==this._itemNumPropName&&e!==this._rootItemPropName&&e!==this._reverseRefMap&&b.push(e);return b},hasAttribute:function(a,b){this._assertIsItem(a);this._assertIsAttribute(b);return b in a},containsValue:function(a,b,e){var c=void 0;"string"===typeof e&&(c=t.patternToRegExp(e,!1));return this._containsValue(a,b,e,c)},_containsValue:function(a,b,e,c){return u.some(this.getValues(a,
b),function(a){if(null!==a&&!n.isObject(a)&&c){if(a.toString().match(c))return!0}else if(e===a)return!0})},isItem:function(a){return a&&a[this._storeRefPropName]===this&&this._arrayOfAllItems[a[this._itemNumPropName]]===a?!0:!1},isItemLoaded:function(a){return this.isItem(a)},loadItem:function(a){this._assertIsItem(a.item)},getFeatures:function(){return this._features},getLabel:function(a){if(this._labelAttr&&this.isItem(a))return this.getValue(a,this._labelAttr)},getLabelAttributes:function(a){return this._labelAttr?
[this._labelAttr]:null},filter:function(a,b,e){var c=[],k,d;if(a.query){var f;k=a.queryOptions?a.queryOptions.ignoreCase:!1;var h={};for(d in a.query)f=a.query[d],"string"===typeof f?h[d]=t.patternToRegExp(f,k):f instanceof RegExp&&(h[d]=f);for(k=0;k<b.length;++k){var l=!0,g=b[k];if(null===g)l=!1;else for(d in a.query)f=a.query[d],this._containsValue(g,d,f,h[d])||(l=!1);l&&c.push(g)}}else for(k=0;k<b.length;++k)d=b[k],null!==d&&c.push(d);e(c,a)},_fetchItems:function(a,b,e){var c=this;if(this._loadFinished)this.filter(a,
this._getItemsArray(a.queryOptions),b);else if(this._jsonFileUrl!==this._ccUrl?(m.deprecated(this.declaredClass+": ","To change the url, set the url property of the store, not _jsonFileUrl.  _jsonFileUrl support will be removed in 2.0"),this.url=this._ccUrl=this._jsonFileUrl):this.url!==this._ccUrl&&(this._ccUrl=this._jsonFileUrl=this.url),null!=this.data&&(this._jsonData=this.data,this.data=null),this._jsonFileUrl)if(this._loadInProgress)this._queuedFetches.push({args:a,filter:n.hitch(c,"filter"),
findCallback:n.hitch(c,b)});else{this._loadInProgress=!0;var k=r.get({url:c._jsonFileUrl,handleAs:"json-comment-optional",preventCache:this.urlPreventCache,failOk:this.failOk});k.addCallback(function(f){try{c._getItemsFromLoadedData(f),c._loadFinished=!0,c._loadInProgress=!1,c.filter(a,c._getItemsArray(a.queryOptions),b),c._handleQueuedFetches()}catch(h){c._loadFinished=!0,c._loadInProgress=!1,e(h,a)}});k.addErrback(function(b){c._loadInProgress=!1;e(b,a)});var d=null;a.abort&&(d=a.abort);a.abort=
function(){var b=k;b&&-1===b.fired&&(b.cancel(),b=null);d&&d.call(a)}}else if(this._jsonData)try{this._loadFinished=!0,this._getItemsFromLoadedData(this._jsonData),this._jsonData=null,c.filter(a,this._getItemsArray(a.queryOptions),b)}catch(f){e(f,a)}else e(Error(this.declaredClass+": No JSON source data was provided as either URL or a nested Javascript object."),a)},_handleQueuedFetches:function(){if(0<this._queuedFetches.length){for(var a=0;a<this._queuedFetches.length;a++){var b=this._queuedFetches[a],
e=b.args,c=b.filter,b=b.findCallback;c?c(e,this._getItemsArray(e.queryOptions),b):this.fetchItemByIdentity(e)}this._queuedFetches=[]}},_getItemsArray:function(a){return a&&a.deep?this._arrayOfAllItems:this._arrayOfTopLevelItems},close:function(a){this.clearOnClose&&this._loadFinished&&!this._loadInProgress&&(this._arrayOfAllItems=[],this._arrayOfTopLevelItems=[],this._loadFinished=!1,this._itemsByIdentity=null,this._loadInProgress=!1,this._queuedFetches=[])},_getItemsFromLoadedData:function(a){function b(a){return null!==
a&&"object"===typeof a&&(!n.isArray(a)||c)&&!n.isFunction(a)&&(a.constructor==Object||n.isArray(a))&&"undefined"===typeof a._reference&&"undefined"===typeof a._type&&"undefined"===typeof a._value&&k.hierarchical}function e(a){k._arrayOfAllItems.push(a);for(var c in a){var d=a[c];if(d)if(n.isArray(d))for(var f=0;f<d.length;++f){var g=d[f];b(g)&&e(g)}else b(d)&&e(d)}}var c=!1,k=this;this._labelAttr=a.label;var d,f;this._arrayOfAllItems=[];this._arrayOfTopLevelItems=a.items;for(d=0;d<this._arrayOfTopLevelItems.length;++d)f=
this._arrayOfTopLevelItems[d],n.isArray(f)&&(c=!0),e(f),f[this._rootItemPropName]=!0;var h={},l;for(d=0;d<this._arrayOfAllItems.length;++d)for(l in f=this._arrayOfAllItems[d],f){if(l!==this._rootItemPropName){var g=f[l];null!==g?n.isArray(g)||(f[l]=[g]):f[l]=[null]}h[l]=l}for(;h[this._storeRefPropName];)this._storeRefPropName+="_";for(;h[this._itemNumPropName];)this._itemNumPropName+="_";for(;h[this._reverseRefMap];)this._reverseRefMap+="_";if(h=a.identifier)for(this._itemsByIdentity={},this._features["dojo.data.api.Identity"]=
h,d=0;d<this._arrayOfAllItems.length;++d)if(f=this._arrayOfAllItems[d],a=f[h],a=a[0],Object.hasOwnProperty.call(this._itemsByIdentity,a)){if(this._jsonFileUrl)throw Error(this.declaredClass+":  The json data as specified by: ["+this._jsonFileUrl+"] is malformed.  Items within the list have identifier: ["+h+"].  Value collided: ["+a+"]");if(this._jsonData)throw Error(this.declaredClass+":  The json data provided by the creation arguments is malformed.  Items within the list have identifier: ["+h+"].  Value collided: ["+
a+"]");}else this._itemsByIdentity[a]=f;else this._features["dojo.data.api.Identity"]=Number;for(d=0;d<this._arrayOfAllItems.length;++d)f=this._arrayOfAllItems[d],f[this._storeRefPropName]=this,f[this._itemNumPropName]=d;for(d=0;d<this._arrayOfAllItems.length;++d)for(l in f=this._arrayOfAllItems[d],f)for(a=f[l],h=0;h<a.length;++h)if(g=a[h],null!==g&&"object"==typeof g){if("_type"in g&&"_value"in g){var m=g._type,p=this._datatypeMap[m];if(p)if(n.isFunction(p))a[h]=new p(g._value);else if(n.isFunction(p.deserialize))a[h]=
p.deserialize(g._value);else throw Error("dojo.data.ItemFileReadStore: Value provided in typeMap was neither a constructor, nor a an object with a deserialize function");else throw Error("dojo.data.ItemFileReadStore: in the typeMap constructor arg, no object class was specified for the datatype '"+m+"'");}if(g._reference){g=g._reference;if(n.isObject(g))for(m=0;m<this._arrayOfAllItems.length;++m){var p=this._arrayOfAllItems[m],q=!0,r;for(r in g)p[r]!=g[r]&&(q=!1);q&&(a[h]=p)}else a[h]=this._getItemByIdentity(g);
this.referenceIntegrity&&(g=a[h],this.isItem(g)&&this._addReferenceToMap(g,f,l))}else this.isItem(g)&&this.referenceIntegrity&&this._addReferenceToMap(g,f,l)}},_addReferenceToMap:function(a,b,e){},getIdentity:function(a){var b=this._features["dojo.data.api.Identity"];return b===Number?a[this._itemNumPropName]:(a=a[b])?a[0]:null},fetchItemByIdentity:function(a){var b,e;if(this._loadFinished)b=this._getItemByIdentity(a.identity),a.onItem&&(e=a.scope?a.scope:m.global,a.onItem.call(e,b));else{var c=this;
this._jsonFileUrl!==this._ccUrl?(m.deprecated(this.declaredClass+": ","To change the url, set the url property of the store, not _jsonFileUrl.  _jsonFileUrl support will be removed in 2.0"),this.url=this._ccUrl=this._jsonFileUrl):this.url!==this._ccUrl&&(this._ccUrl=this._jsonFileUrl=this.url);null!=this.data&&null==this._jsonData&&(this._jsonData=this.data,this.data=null);this._jsonFileUrl?this._loadInProgress?this._queuedFetches.push({args:a}):(this._loadInProgress=!0,e=r.get({url:c._jsonFileUrl,
handleAs:"json-comment-optional",preventCache:this.urlPreventCache,failOk:this.failOk}),e.addCallback(function(e){var d=a.scope?a.scope:m.global;try{c._getItemsFromLoadedData(e),c._loadFinished=!0,c._loadInProgress=!1,b=c._getItemByIdentity(a.identity),a.onItem&&a.onItem.call(d,b),c._handleQueuedFetches()}catch(f){c._loadInProgress=!1,a.onError&&a.onError.call(d,f)}}),e.addErrback(function(b){c._loadInProgress=!1;a.onError&&a.onError.call(a.scope?a.scope:m.global,b)})):this._jsonData&&(c._getItemsFromLoadedData(c._jsonData),
c._jsonData=null,c._loadFinished=!0,b=c._getItemByIdentity(a.identity),a.onItem&&(e=a.scope?a.scope:m.global,a.onItem.call(e,b)))}},_getItemByIdentity:function(a){var b=null;this._itemsByIdentity?Object.hasOwnProperty.call(this._itemsByIdentity,a)&&(b=this._itemsByIdentity[a]):Object.hasOwnProperty.call(this._arrayOfAllItems,a)&&(b=this._arrayOfAllItems[a]);void 0===b&&(b=null);return b},getIdentityAttributes:function(a){a=this._features["dojo.data.api.Identity"];return a===Number?null:[a]},_forceLoad:function(){var a=
this;this._jsonFileUrl!==this._ccUrl?(m.deprecated(this.declaredClass+": ","To change the url, set the url property of the store, not _jsonFileUrl.  _jsonFileUrl support will be removed in 2.0"),this.url=this._ccUrl=this._jsonFileUrl):this.url!==this._ccUrl&&(this._ccUrl=this._jsonFileUrl=this.url);null!=this.data&&(this._jsonData=this.data,this.data=null);if(this._jsonFileUrl){var b=r.get({url:this._jsonFileUrl,handleAs:"json-comment-optional",preventCache:this.urlPreventCache,failOk:this.failOk,
sync:!0});b.addCallback(function(b){try{if(!0!==a._loadInProgress&&!a._loadFinished)a._getItemsFromLoadedData(b),a._loadFinished=!0;else if(a._loadInProgress)throw Error(this.declaredClass+":  Unable to perform a synchronous load, an async load is in progress.");}catch(c){throw c;}});b.addErrback(function(a){throw a;})}else this._jsonData&&(a._getItemsFromLoadedData(a._jsonData),a._jsonData=null,a._loadFinished=!0)}});n.extend(q,w);return q});
//# sourceMappingURL=ItemFileReadStore.js.map