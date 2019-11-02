@sm_main = global i32 0
define void @.init.sm_main() {
    %.0 = add i32 30, 12
    %.1 = sub i32 15, 2
    %.2 = sdiv i32 %.1, 3
    %.3 = add i32 %.2, 7
    %.4 = sub i32 %.3, 2
    %.5 = mul i32 %.0, %.4
    %.6 = sub i32 0, 1
    %.7 = mul i32 77, %.6
    %.8 = sub i32 %.5, %.7
    %.9 = sub i32 %.8, 42
    store i32 %.9, i32* @sm_main
    ret void
}

define void @sm_init() {
    call void @.init.sm_main()
    ret void
}
