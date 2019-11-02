@a = global i32 0
define void @.init.a() {
    store i32 1, i32* @a
    ret void
}

@b = global i32 0
define void @.init.b() {
    store i32 1, i32* @b
    ret void
}

@c = global i32 0
define void @.init.c() {
    %.0 = load i32, i32* @b
    %.1 = load i32, i32* @a
    %.2 = add i32 %.0, %.1
    store i32 %.2, i32* @c
    ret void
}

@d = global i32 0
define void @.init.d() {
    %.0 = load i32, i32* @c
    %.1 = load i32, i32* @b
    %.2 = add i32 %.0, %.1
    store i32 %.2, i32* @d
    ret void
}

@e = global i32 0
define void @.init.e() {
    %.0 = load i32, i32* @d
    %.1 = load i32, i32* @c
    %.2 = add i32 %.0, %.1
    store i32 %.2, i32* @e
    ret void
}

@f = global i32 0
define void @.init.f() {
    %.0 = load i32, i32* @a
    %.1 = load i32, i32* @b
    %.2 = add i32 %.0, %.1
    %.3 = load i32, i32* @c
    %.4 = add i32 %.2, %.3
    %.5 = load i32, i32* @d
    %.6 = add i32 %.4, %.5
    %.7 = load i32, i32* @e
    %.8 = add i32 %.6, %.7
    store i32 %.8, i32* @f
    ret void
}

define i32 @g(i32 %a, i32 %b, i32 %c, i32 %d) {
    %.0 = add i32 %a, %b
    %.1 = add i32 %.0, %c
    %.2 = add i32 %.1, %d
    ret i32 %.2
}

@sm_main = global i32 0
define void @.init.sm_main() {
    %.0 = call i32 @g(i32 2, i32 3, i32 4, i32 5)
    %.1 = load i32, i32* @f
    %.2 = mul i32 %.1, 2
    %.3 = sub i32 %.0, %.2
    store i32 %.3, i32* @sm_main
    ret void
}

define void @sm_init() {
    call void @.init.a()
    call void @.init.b()
    call void @.init.c()
    call void @.init.d()
    call void @.init.e()
    call void @.init.f()
    call void @.init.sm_main()
    ret void
}
