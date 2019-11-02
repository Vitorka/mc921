@a = global i32 0
define void @.init.a() {
    store i32 1, i32* @a
    ret void
}

@b = global i32 0
define void @.init.b() {
    %.0 = load i32, i32* @a
    %.1 = load i32, i32* @a
    %.2 = mul i32 2, %.1
    %.3 = add i32 %.0, %.2
    %.4 = add i32 %.3, 3
    %.5 = sub i32 %.4, 2
    store i32 %.5, i32* @b
    ret void
}

define i32 @f1(i32 %a) {
    %.0 = load i32, i32* @b
    %.1 = mul i32 %.0, 8
    %.2 = add i32 3, %a
    %.3 = sdiv i32 %.1, %.2
    ret i32 %.3
}

define i32 @f2(i32 %b) {
    %.0 = load i32, i32* @a
    %.1 = mul i32 %b, %.0
    %.2 = sub i32 %b, 2
    %.3 = mul i32 %b, %.2
    %.4 = add i32 %.1, %.3
    ret i32 %.4
}

@c = global i32 0
define void @.init.c() {
    %.0 = call i32 @f1(i32 2)
    %.1 = load i32, i32* @a
    %.2 = call i32 @f2(i32 %.1)
    %.3 = add i32 %.0, %.2
    %.4 = sub i32 %.3, 15
    %.5 = sdiv i32 %.4, 2
    store i32 %.5, i32* @c
    ret void
}

define i32 @f3(i32 %a, i32 %b) {
    %.0 = load i32, i32* @c
    %.1 = sub i32 %.0, %b
    %.2 = mul i32 %a, %.1
    ret i32 %.2
}

@d = global i32 0
define void @.init.d() {
    %.0 = sub i32 0, 10
    store i32 %.0, i32* @d
    ret void
}

@sm_main = global i32 0
define void @.init.sm_main() {
    %.0 = load i32, i32* @c
    %.1 = load i32, i32* @d
    %.2 = call i32 @f3(i32 %.0, i32 %.1)
    store i32 %.2, i32* @sm_main
    ret void
}

define void @sm_init() {
    call void @.init.a()
    call void @.init.b()
    call void @.init.c()
    call void @.init.d()
    call void @.init.sm_main()
    ret void
}
