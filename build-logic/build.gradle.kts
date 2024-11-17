plugins{
    `kotlin-dsl`
}

dependencies{
    implementation(libs.bundles.gradleplugins)
    implementation(files(libs.javaClass.superclass.protectionDomain.codeSource.location))
}