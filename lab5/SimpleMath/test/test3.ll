@a = global i32 0
define void @.init.a() {
    store i32 5, i32* @a
    ret void
}

@b = global i32 0
define void @.init.b() {
    %.0 = load i32, i32* @a
    %.1 = sdiv i32 30, %.0
    %.2 = add i32 %.1, 2
    store i32 %.2, i32* @b
    ret void
}

define i32 @f(i32 %a) {
    %.0 = load i32, i32* @b
    %.1 = add i32 %a, %.0
    %.2 = mul i32 3, 3
    %.3 = sub i32 %.1, %.2
    ret i32 %.3
}

@sm_main = global i32 0
define void @.init.sm_main() {
    %.0 = load i32, i32* @a
    %.1 = add i32 5, %.0
    %.2 = load i32, i32* @b
    %.3 = call i32 @f(i32 %.2)
    %.4 = sub i32 %.1, %.3
    store i32 %.4, i32* @sm_main
    ret void
}

define void @sm_init() {
    call void @.init.a()
    call void @.init.b()
    call void @.init.sm_main()
    ret void
}
