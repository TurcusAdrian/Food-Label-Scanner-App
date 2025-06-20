package com.example.food_label_scanner.barcode_functionality

import android.util.Log
import com.example.food_label_scanner.DBHelper


fun levenshteinDistance(s1: String, s2: String): Int {
    val len1 = s1.length
    val len2 = s2.length

    // Create a matrix to store distances
    val dp = Array(len1 + 1) { IntArray(len2 + 1) }

    // Initialize the first row and column
    for (i in 0..len1) {
        dp[i][0] = i
    }
    for (j in 0..len2) {
        dp[0][j] = j
    }

    // Fill the matrix
    for (i in 1..len1) {
        for (j in 1..len2) {
            val cost = if (s1[i - 1].lowercaseChar() == s2[j - 1].lowercaseChar()) 0 else 1
            dp[i][j] = minOf(
                dp[i - 1][j] + 1,      // Deletion
                dp[i][j - 1] + 1,      // Insertion
                dp[i - 1][j - 1] + cost // Substitution
            )
        }
    }

    return dp[len1][len2]
}

// Function to return UPPERCASE string:
fun uppercase_text(ingredients: String): String {
    return ingredients.uppercase()
}

//Removes percentage and percentage numbers if they are not preceded by "E":
fun removePercentageNumbers(ingredients: String): String {
    val regex = """(?<!E\d)\s*\d+(?:[.,]\d+)?\s*(?:%?\s*)"""
    return ingredients.replace(Regex(regex, RegexOption.IGNORE_CASE), "")
}

//Extracts the text after "INGREDIENTE:" and before ".":
fun extractIngredientsSection(text: String): String {
    val keyword = "INGREDIENTE:"
    val startIndex = text.indexOf(keyword, ignoreCase = true)

    if (startIndex == -1) {
        Log.w("extractIngredientsSection", "INGREDIENTE: not found in text: '$text'")
        return ""
    }

    val endOfKeywordIndex = startIndex + keyword.length
    var remainingText = text.substring(endOfKeywordIndex).trim()

    // Remove percentage numbers first
    remainingText = removePercentageNumbers(remainingText)

    // Stop extraction at the first stop keyword or period
    val stopKeywords = listOf(
        ",poate contine",
        "poate contine",
        "poate conține",
        "poate include",
        "poate avea",
        "alergeni",
        "traces of",
        "may contain"
    )

    var cutIndex = remainingText.length
    for (stopWord in stopKeywords) {
        val index = remainingText.indexOf(stopWord, ignoreCase = true)
        if (index != -1 && index < cutIndex) {
            cutIndex = index
            break
        }
    }

    val periodIndex = remainingText.indexOf('.')
    if (periodIndex != -1 && periodIndex < cutIndex) {
        cutIndex = periodIndex
    }

    if (cutIndex < remainingText.length) {
        remainingText = remainingText.substring(0, cutIndex).trim()
    }
    remainingText = remainingText.replace(Regex("\\s*\\(\\s*\\)\\s*"), "")

    Log.d("extractIngredientsSection", "Clean ingredients: '$remainingText'")
    return remainingText
}



//Removes any diacritics or symbols:
fun remove_diacritics(ingredients: String): String {
    val correctedIngredients = StringBuilder()

    if (ingredients.equals("quinoa", ignoreCase = true)) {
        return ingredients // Return the original string without changes
    }

    for (char in ingredients) {
        when (char) {
            // A / a
            'à', 'á', 'â', 'ã', 'ä', 'å', 'ā', 'ă', 'ą', 'ǎ', 'ȧ', 'ạ', 'ả', 'ấ', 'ầ', 'ẩ', 'ẫ', 'ậ', 'ắ', 'ằ', 'ẳ', 'ẵ', 'ặ' -> correctedIngredients.append('a')
            'À', 'Á', 'Â', 'Ã', 'Ä', 'Å', 'Ā', 'Ă', 'Ą', 'Ǎ', 'Ȧ', 'Ạ', 'Ả', 'Ấ', 'Ầ', 'Ẩ', 'Ẫ', 'Ậ', 'Ắ', 'Ằ', 'Ẳ', 'Ẵ', 'Ặ' -> correctedIngredients.append('A')

            // C / c
            'ç', 'ć', 'ĉ', 'ċ', 'č' -> correctedIngredients.append('c')
            'Ç', 'Ć', 'Ĉ', 'Ċ', 'Č' -> correctedIngredients.append('C')

            // D / d
            'ď', 'đ' -> correctedIngredients.append('d')
            'Ď', 'Đ' -> correctedIngredients.append('D')

            // E / e
            'è', 'é', 'ê', 'ë', 'ē', 'ĕ', 'ė', 'ę', 'ě', 'ẹ', 'ẻ', 'ẽ', 'ế', 'ề', 'ể', 'ễ', 'ệ' -> correctedIngredients.append('e')
            'È', 'É', 'Ê', 'Ë', 'Ē', 'Ĕ', 'Ė', 'Ę', 'Ě', 'Ẹ', 'Ẻ', 'Ẽ', 'Ế', 'Ề', 'Ể', 'Ễ', 'Ệ' -> correctedIngredients.append('E')

            // G / g
            'ĝ', 'ğ', 'ġ', 'ģ' -> correctedIngredients.append('g')
            'Ĝ', 'Ğ', 'Ġ', 'Ģ' -> correctedIngredients.append('G')

            // H / h
            'ĥ', 'ħ' -> correctedIngredients.append('h')
            'Ĥ', 'Ħ' -> correctedIngredients.append('H')

            // I / i
            'ì', 'í', 'î', 'ï', 'ĩ', 'ī', 'ĭ', 'į', 'ı', 'ǐ', 'ȉ', 'ȋ', 'ị', 'ỉ' -> correctedIngredients.append('i')
            'Ì', 'Í', 'Î', 'Ï', 'Ĩ', 'Ī', 'Ĭ', 'Į', 'İ', 'Ǐ', 'Ȉ', 'Ȋ', 'Ị', 'Ỉ' -> correctedIngredients.append('I')

            // J / j
            'ĵ' -> correctedIngredients.append('j')
            'Ĵ' -> correctedIngredients.append('J')

            // K / k
            'ķ', 'ĸ' -> correctedIngredients.append('k')
            'Ķ' -> correctedIngredients.append('K')

            // L / l
            'ĺ', 'ļ', 'ľ', 'ŀ', 'ł' -> correctedIngredients.append('l')
            'Ĺ', 'Ļ', 'Ľ', 'Ŀ', 'Ł' -> correctedIngredients.append('L')

            // N / n
            'ñ', 'ń', 'ņ', 'ň', 'ŋ' -> correctedIngredients.append('n')
            'Ñ', 'Ń', 'Ņ', 'Ň', 'Ŋ' -> correctedIngredients.append('N')

            // O / o
            'ò', 'ó', 'ô', 'õ', 'ö', 'ø', 'ō', 'ŏ', 'ő', 'ǒ', 'ȯ', 'ọ', 'ỏ', 'ố', 'ồ', 'ổ', 'ỗ', 'ộ', 'ớ', 'ờ', 'ở', 'ỡ', 'ợ' -> correctedIngredients.append('o')
            'Ò', 'Ó', 'Ô', 'Õ', 'Ö', 'Ø', 'Ō', 'Ŏ', 'Ő', 'Ǒ', 'Ȯ', 'Ọ', 'Ỏ', 'Ố', 'Ồ', 'Ổ', 'Ỗ', 'Ộ', 'Ớ', 'Ờ', 'Ở', 'Ỡ', 'Ợ' -> correctedIngredients.append('O')

            // Q / q
            'q' -> correctedIngredients.append('g')
            'Q' -> correctedIngredients.append('G')

            // R / r
            'ŕ', 'ŗ', 'ř' -> correctedIngredients.append('r')
            'Ŕ', 'Ŗ', 'Ř' -> correctedIngredients.append('R')

            // S / s
            'ś', 'ŝ', 'ş', 'š', 'ș' -> correctedIngredients.append('s')
            'Ś', 'Ŝ', 'Ş', 'Š', 'Ș' -> correctedIngredients.append('S')

            // T / t
            'ţ', 'ť', 'ŧ', 'ț' -> correctedIngredients.append('t')
            'Ţ', 'Ť', 'Ŧ', 'Ț' -> correctedIngredients.append('T')

            // U / u
            'ù', 'ú', 'û', 'ü', 'ũ', 'ū', 'ŭ', 'ů', 'ű', 'ų', 'ǔ', 'ȕ', 'ȗ', 'ụ', 'ủ', 'ứ', 'ừ', 'ử', 'ữ', 'ự' -> correctedIngredients.append('u')
            'Ù', 'Ú', 'Û', 'Ü', 'Ũ', 'Ū', 'Ŭ', 'Ů', 'Ű', 'Ų', 'Ǔ', 'Ȕ', 'Ȗ', 'Ụ', 'Ủ', 'Ứ', 'Ừ', 'Ử', 'Ữ', 'Ự' -> correctedIngredients.append('U')

            // W / w
            'ŵ' -> correctedIngredients.append('w')
            'Ŵ' -> correctedIngredients.append('W')

            // Y / y
            'ý', 'ÿ', 'ŷ', 'ỳ', 'ỵ', 'ỷ', 'ỹ' -> correctedIngredients.append('y')
            'Ý', 'Ÿ', 'Ŷ', 'Ỳ', 'Ỵ', 'Ỷ', 'Ỹ' -> correctedIngredients.append('Y')

            // Z / z
            'ź', 'ż', 'ž' -> correctedIngredients.append('z')
            'Ź', 'Ż', 'Ž' -> correctedIngredients.append('Z')

            // Your special cases
            '!' -> correctedIngredients.append("")
            '?' -> correctedIngredients.append("")
            '*' -> correctedIngredients.append("")
            '_' -> correctedIngredients.append("")
            '-' -> correctedIngredients.append(" ")

            '"' -> correctedIngredients.append("")
            '\'' -> correctedIngredients.append("")
            else -> correctedIngredients.append(char)
        }
    }

    return correctedIngredients.toString()
}

//Extracts the content between ():
fun extractParenthesisContent(text: String): String {
    val start = text.indexOf("(")
    val end = text.indexOf(")")
    return if (start != -1 && end != -1 && end > start) {
        text.substring(start + 1, end)
    } else {
        ""
    }
}

//Splits the text by "," to determine individual ingredients:
fun splitIngredients(processedIngredients: String): List<String> {
    // 1. Split the string by the comma delimiter
    val ingredientsList = processedIngredients.split(",")

    // 2. Trim whitespace from each ingredient
    val trimmedIngredientsList = ingredientsList.map { it.trim() }

    return trimmedIngredientsList
}

fun process_ingredients(ingredients: String): String {
    val withoutPercentage = removePercentageNumbers(ingredients)
    val withoutDiacritics = remove_diacritics(withoutPercentage)
    return uppercase_text(withoutDiacritics)
}

//Splits the text by "," to determine individual ingredients between parenthesis (ingredient1, ingredient2):
fun splitIngredientsAdvanced(text: String): List<String> {
    val result = mutableListOf<String>()
    var current = StringBuilder()
    var parenthesesLevel = 0

    for (char in text) {
        when (char) {
            '(' -> {
                parenthesesLevel++
                current.append(char)
            }
            ')' -> {
                parenthesesLevel--
                current.append(char)
            }
            ',' -> {
                if (parenthesesLevel == 0) {
                    result.add(current.toString().trim())
                    current = StringBuilder()
                } else {
                    current.append(char)
                }
            }
            else -> current.append(char)
        }
    }
    if (current.isNotBlank()) {
        result.add(current.toString().trim())
    }
    return result
}

val ignoredKeywords = setOf(
    "EMULSIFIANTI", "CONSERVANTI", "AGENTI DE CRESTERE", "COLORANTI",
    "REGULATOR DE ACIDITATE", "STABILIZATORI", "EMULGATOR:", "AROME", "INGREDIENT:", "EMULSIFIANT:","VOPSEA:"
)




fun extractValidIngredients(rawIngredients: List<String>, dbHelper: DBHelper): List<String> {
    val validIngredients = mutableListOf<String>()
    val ingredientPattern = Regex("[A-Z][A-Z\\s()]+")
    for (ingredient in rawIngredients) {
        val cleaned = ingredient.uppercase().replace(
            Regex(
                "^(CONSERVANT(I)?|CORECTOR DE ACIDITATE:|" +
                        "EMULGATOR(I)?|EMULSIFIANT(I)?|COLORANT(I)?|" +
                        "REGULATOR DE ACIDITATE|AGENT(I)? DE CRESTERE|VOPSEA|" +
                        "AGENT(I)? DE GLAZURARE|AGENT(I)? DE UMEZIRE|" +
                        "AGENT(I)? DE INGROSARE):?\\s*"
            ), ""
        )
        val plain = cleaned.substringBefore("(").trim()
        val inner = extractParenthesisContent(cleaned)
        // Check the plain keyword before the parenthesis
        if (ingredientPattern.matches(plain) && dbHelper.getIngredientByNameFuzzy(plain) != null) {
            validIngredients.add(plain)
        }
        // Check ingredients inside parentheses (if any)
        if (inner.isNotEmpty()) {
            val nestedIngredients = splitIngredientsAdvanced(inner)
            for (nested in nestedIngredients) {
                val name = nested.trim()
                if (ingredientPattern.matches(name) && dbHelper.getIngredientByNameFuzzy(name) != null) {
                    validIngredients.add(name)
                }
            }
        }
    }
    return validIngredients.distinct()
}

//Retrieves the details for each ingredient extracted/found in Database:
fun getIngredientDetails(ingredients: List<String>, dbHelper: DBHelper): List<String> {
    val details = mutableListOf<String>()
    for (ingredientName in ingredients) {
        val ingredientData = dbHelper.getIngredientByNameFuzzy(ingredientName)
        if (ingredientData != null) {
            val name = ingredientData["name"]
            val nutritionalValue = ingredientData["nutritional_value"]
            val categoryId = ingredientData["category_id"] as Int
            val healthRating = ingredientData["health_rating"]
            val description = ingredientData["description"]
            val categoryName = dbHelper.getCategoryNameById(categoryId) ?: "Category not found"
            val detail = "Name: $name, Nutritional Value: $nutritionalValue, Category: $categoryName, Health Rating: $healthRating, Description: $description"
            details.add(detail)
        } else {
            details.add("Ingredient '$ingredientName' not found in database")
        }
    }
    return details.distinct()
}