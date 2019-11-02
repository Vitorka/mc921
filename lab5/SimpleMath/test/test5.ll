@a = global i32 0
define void @.init.a() {
    store i32 10, i32* @a
    ret void
}

@b = global i32 0
define void @.init.b() {
    store i32 5, i32* @b
    ret void
}

define i32 @function(i32 %c) {
    %.0 = load i32, i32* @a
    %.1 = load i32, i32* @b
    %.2 = mul i32 %.1, 2
    %.3 = add i32 %.0, %.2
    %.4 = sdiv i32 %.3, %c
    ret i32 %.4
}

@sm_main = global i32 0
define void @.init.sm_main() {
    %.0 = call i32 @function(i32 10)
    %.1 = sub i32 %.0, 1
    store i32 %.1, i32* @sm_main
    ret void
}

define void @sm_init() {
    call void @.init.a()
    call void @.init.b()
    call void @.init.sm_main()
    ret void
}
