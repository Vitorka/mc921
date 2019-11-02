define i32 @a(i32 %x) {
    %.0 = add i32 %x, 10
    %.1 = sdiv i32 %.0, 2
    ret i32 %.1
}

@b = global i32 0
define void @.init.b() {
    %.0 = call i32 @a(i32 2)
    store i32 %.0, i32* @b
    ret void
}

define i32 @c(i32 %x) {
    %.0 = call i32 @a(i32 7)
    %.1 = load i32, i32* @b
    %.2 = sub i32 %.0, %.1
    %.3 = mul i32 %.2, 2
    ret i32 %.3
}

@d = global i32 0
define void @.init.d() {
    %.0 = call i32 @c(i32 3)
    %.1 = call i32 @a(i32 2)
    %.2 = sub i32 %.0, %.1
    %.3 = call i32 @c(i32 2)
    %.4 = call i32 @a(i32 3)
    %.5 = add i32 %.3, %.4
    %.6 = mul i32 %.2, %.5
    store i32 %.6, i32* @d
    ret void
}

define i32 @e(i32 %a, i32 %b) {
    %.0 = add i32 %b, %a
    %.1 = load i32, i32* @d
    %.2 = call i32 @c(i32 %.1)
    %.3 = mul i32 %.0, %.2
    %.4 = add i32 %.3, 15
    %.5 = sub i32 0, 5
    %.6 = sdiv i32 %.4, %.5
    ret i32 %.6
}

@sm_main = global i32 0
define void @.init.sm_main() {
    %.0 = load i32, i32* @d
    %.1 = call i32 @e(i32 10, i32 %.0)
    %.2 = load i32, i32* @b
    %.3 = call i32 @c(i32 %.2)
    %.4 = add i32 %.1, %.3
    store i32 %.4, i32* @sm_main
    ret void
}

define void @sm_init() {
    call void @.init.b()
    call void @.init.d()
    call void @.init.sm_main()
    ret void
}
