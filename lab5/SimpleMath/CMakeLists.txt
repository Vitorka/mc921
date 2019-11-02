add_llvm_library( LLVMSimpleMath MODULE
  SimpleMath.cpp

  DEPENDS
  PLUGIN_TOOL
  opt
  )
