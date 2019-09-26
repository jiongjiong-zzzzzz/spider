import execjs
js_1 = '''
function decode(){
    return String.fromCodePoint('22825')
}
'''
ctx = execjs.compile(js_1)
js_2 = ctx.call("decode")
print(js_2)
