# Search Guidelines for Android Project
When searching for code:
- Use `rg` (ripgrep) instead of `find` or `grep`
- Use file type filters for Android development
- Add context: `rg "pattern" -C 3`
- Limit results: `rg "pattern" --max-count 10`
- Always exclude build folders: `rg "pattern" -g "!**/build/**"`

## Android File Types
- Kotlin: `--type kotlin`
- Java: `--type java`
- XML: `--type xml`
- Gradle: `--type gradle`
- JSON: `--type json`

## Finding Files by Name
- Find specific Kotlin files: `rg --files --type kotlin --glob "*ViewModel.kt" -g "!**/build/**"`
- Find Activity files: `rg --files --type kotlin --type java --glob "*Activity.*" -g "!**/build/**"`
- Find layout files: `rg --files --type xml --glob "*layout*.xml" -g "!**/build/**"`
- Find resource files: `rg --files --type xml -g "**/res/**" -g "!**/build/**"`
- Find manifest files: `rg --files --glob "*Manifest.xml" -g "!**/build/**"`

## Common Android Search Patterns
- Find Activities: `rg "class.*Activity" --type kotlin --type java -g "!**/build/**"`
- Find ViewModels: `rg "ViewModel" --type kotlin -C 2 -g "!**/build/**"`
- Find Fragments: `rg "Fragment" --type kotlin --type java -C 2 -g "!**/build/**"`
- Find Composables: `rg "@Composable" --type kotlin -C 2 -g "!**/build/**"`
- Find API calls: `rg "retrofit|okhttp|api" --type kotlin --type java -i -C 2 -g "!**/build/**"`
- Find database code: `rg "room|dao|entity|database" --type kotlin --type java -i -C 2 -g "!**/build/**"`
- Find dependency injection: `rg "dagger|hilt|inject" --type kotlin --type java -i -C 2 -g "!**/build/**"`

## Android Directory-Specific Searches
- Search all Kotlin/Java code: `rg "pattern" --type kotlin --type java -g "!**/build/**"`
- Search all XML resources: `rg "pattern" --type xml -g "!**/build/**"`
- Search only test files: `rg "pattern" --type kotlin --type java -g "**/test/**" -g "!**/build/**"`
- Search only main source: `rg "pattern" --type kotlin --type java -g "**/main/**" -g "!**/build/**"`
- Search layout files: `rg "pattern" --type xml -g "**/layout/**" -g "!**/build/**"`
- Search values files: `rg "pattern" --type xml -g "**/values/**" -g "!**/build/**"`

## Build and Config Files
- Find Gradle configurations: `rg "pattern" --type gradle -g "!**/build/**"`
- Find dependencies: `rg "implementation|api|compile" --type gradle -g "!**/build/**"`
- Find version info: `rg "versionCode|versionName" --type gradle --type xml -g "!**/build/**"`
- Find permissions: `rg "permission" --type xml -g "**/AndroidManifest.xml"`

## Performance Tips for Large Android Projects
- Always exclude build directories: `rg "pattern" -g "!**/build/**"`
- Use file type filters: `rg "pattern" --type kotlin --type java`
- Search by file extensions when needed: `rg "pattern" -g "*.kt" -g "*.java" -g "!**/build/**"`
- Use case-insensitive for broader searches: `rg "pattern" -i --type kotlin -g "!**/build/**"`
- Exclude other generated dirs if needed: `rg "pattern" -g "!**/build/**" -g "!**/.gradle/**"`

## Examples
- Find authentication logic: `rg "auth|login|token" --type kotlin -i -C 2 -g "!**/build/**"`
- Find network models: `rg "data class.*Response" --type kotlin -C 2 -g "!**/build/**"`
- Find string resources: `rg "string name=" --type xml -g "**/values/**" -g "!**/build/**"`
- Find UI components: `rg "Button|TextView|RecyclerView" --type kotlin --type xml -C 2 -g "!**/build/**"`
- Find navigation: `rg "navigate|findNavController" --type kotlin -C 2 -g "!**/build/**"`