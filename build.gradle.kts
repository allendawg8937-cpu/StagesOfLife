// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
  alias(libs.plugins.android.application) 
  aalias(libs.plugins.kotlin.compose) apply true
  alias(libs.plugins.secrets) apply true
  alias(libs.plugins.google.services) apply true
}
