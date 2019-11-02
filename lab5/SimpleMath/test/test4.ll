define i32 @f(i32 %a, i32 %b, i32 %c, i32 %d) {
    %.0 = mul i32 %a, %a
    %.1 = sdiv i32 %.0, %b
    %.2 = mul i32 %c, %c
    %.3 = sdiv i32 %.2, %d
    %.4 = add i32 %.1, %.3
    %.5 = sub i32 %.4, %d
    ret i32 %.5
}

@sm_main = global i32 0
define void @.init.sm_main() {
    %.0 = call i32 @f(i32 5, i32 6, i32 7, i32 8)
    store i32 %.0, i32* @sm_main
    ret void
}

define void @sm_init() {
    call void @.init.sm_main()
    ret void
}
