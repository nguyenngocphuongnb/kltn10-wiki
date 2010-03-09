/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package demo;

/**
 *
 * @author tuandom
 */
public class RemoveSign {
public String removeUnicodeSign(String s) {
        char[] arrChar = {'a', 'A', 'd', 'D', 'e', 'E', 'i', 'I', 'o', 'O', 'u', 'U', 'y', 'Y'};
        char[][] uniChar = {
            {'á', 'à', 'ả', 'ã', 'ạ', 'â', 'ấ', 'ầ', 'ẩ', 'ẫ', 'ậ', 'ă', 'ắ', 'ằ', 'ẳ', 'ẵ', 'ặ'},
            {'Á', 'À', 'Ả', 'Ã', 'Ạ', 'Â', 'Ấ', 'Ầ', 'Ẩ', 'Ẫ', 'Ă', 'Ặ', 'Ắ', 'Ằ', 'Ẳ', 'Ẵ', 'Ặ'},
            {'đ'},
            {'Đ'},
            {'é', 'è', 'ẻ', 'ẽ', 'ẹ', 'ê', 'ế', 'ề', 'ể', 'ễ', 'ệ'},
            {'É', 'È', 'Ẻ', 'Ẽ', 'Ẹ', 'Ê', 'Ế', 'Ề', 'Ể', 'Ễ', 'Ệ'},
            {'í', 'ì', 'ỉ', 'ĩ', 'ị'},
            {'Í', 'Ì', 'Ỉ', 'Ĩ', 'Ị'},
            {'ó', 'ò', 'ỏ', 'õ', 'ọ', 'ô', 'ố', 'ồ', 'ổ', 'ỗ', 'ộ', 'ơ', 'ớ', 'ờ', 'ỡ', 'ợ'},
            {'Ó', 'Ò', 'Ỏ', 'Õ', 'Ọ', 'Ô', 'Ố', 'Ồ', 'Ổ', 'Ỗ', 'Ộ', 'Ơ', 'Ớ', 'Ờ', 'Ỡ', 'Ợ'},
            {'ú', 'ù', 'ủ', 'ũ', 'ụ', 'ư', 'ứ', 'ừ', 'ử', 'ữ', 'ự'},
            {'Ú', 'Ù', 'Ủ', 'Ũ', 'Ụ', 'Ư', 'Ứ', 'Ừ', 'Ử', 'Ữ', 'Ự'},
            {'ý', 'ỳ', 'ỷ', 'ỹ', 'ỵ'},
            {'Ý', 'Ỳ', 'Ỷ', 'Ỹ', 'Ỵ'}};

        for (int i = 0; i < uniChar.length; i++) {
            for (int j = 0; j < uniChar[i].length; j++) {
                s = s.replace(uniChar[i][j], arrChar[i]);
            }
        }
        return s;
    }
public String removeVNIWindowsSign(String s) {
        char[] arrChar = {'a', 'A',  'd', 'D', 'e', 'E', 'i', 'I', 'o', 'O', 'u', 'U', 'y', 'Y'};


        String[][] uniChar = {
            {"aù", "aø", "aû", "aõ", "aï", "aê", "aâ", "aé", "aè", "aú", "aü", "aë", "aá", "aà", "aå", "aã", "aä"},
            {"AÙ", "AØ", "AÛ", "AÕ", "AÏ", "AÊ", "AÂ", "AÉ", "AÈ", "AÚ", "AÜ", "AË", "AÁ", "AÀ", "AÅ", "AÃ", "AÄ", "Aù", "Aø", "Aû", "Aõ", "Aï", "Aê", "Aâ"},

            {"ñ"},
            {"Ñ"},

            {"eù", "eø", "eû", "eõ", "eï", "eâ", "eá", "eà", "eå", "eã", "eä", "eâ"},
            {"EÙ", "EØ", "EÛ", "EÕ", "EÏ", "EÊ", "EÁ", "EÀ", "EÅ", "EÃ", "EÄ", "Eù", "Eø", "Eû", "Eõ", "Eï", "Eê"},
            
            { "í", "ì", "æ", "ó", "ò"},
            {"Í", "Ì", "Æ", "Ó","Ò"},

            {"où", "oø", "oû", "oõ", "oï", "oâ", "ôù", "ôø", "ôû", "ôõ", "ôï", "oá", "oà", "oã", "oä", "oå", "ô"},
            {"OÙ", "OØ", "OÛ", "OÕ", "OÏ", "Ô", "OÂ", "ÔÙ", "ÔØ", "OÅ", "ÔÕ", "ÔÏ", "OÁ", "OÀ", "OÅ", "OÃ", "OÄ", "Où", "Oø", "Oû", "Oõ", "Oï", "Oâ"},
            
            {"uù", "uø", "uû", "uõ", "uï", "öù", "öø", "öû", "öõ", "öï", "ö"},
            {"Uù", "Uø", "Uû", "Uõ", "Uï", "ÖÙ", "ÖØ", "ÖÛ", "ÖÕ", "ÖÏ", "Ö","UÙ", "UØ", "UÛ", "UÕ", "UÏ"},
            
            {"yù", "Yø", "yû", "yõ", "î"},
            {"YÙ", "YØ", "YÛ", "YÕ", "Î"}
            
            };
        for (int i = 0; i < uniChar.length; i++) {
            for (int j = 0; j < uniChar[i].length; j++) {
                s = s.replaceAll(uniChar[i][j], Character.toString(arrChar[i]));
            }
        }
        return s;
    }
public String removeTCVNSign(String s) {
        char[] arrChar = {'a', 'A',  'd', 'D', 'e', 'E', 'i', 'I', 'o', 'O', 'u', 'U', 'y', 'Y'};


        String[][] uniChar = {
            {"¸", "µ", "¶", "·", "¹", "¨", "©", "¾", "»", "¼", "½", "Æ", "Ê", "Ç", "È", "É", "Ë"},
            {"¸", "µ", "¶", "·", "¹", "¡", "¢", "¾", "»", "¼", "½", "Æ", "Ê", "Ç", "È", "É", "Ë"},

            {"®"},
            {"§"},

            {"Ð", "Ì", "Î", "Ï", "Ñ", "ª", "Õ", "Ò", "Ó", "Ô", "Ö"},
            {"Ð", "Ì", "Î", "Ï", "Ñ", "£", "Õ", "Ò", "Ó", "Ô", "Ö"},

            { "Ý", "×", "Ø", "Ü", "Þ"},
            {"Ý", "×", "Ø", "Ü","Þ"},

            {"ã", "ß", "á", "â", "ä", "¥", "¤", "í", "ê", "ë", "ì", "î", "è", "å", "æ", "ç", "é"},
            {"ã", "ß", "á", "â", "ä", "¬", "«", "í", "ê", "ë", "ì", "î", "è", "å", "æ", "ç", "é"},

            {"ó", "ï", "ñ", "ò", "ô", "­", "ø", "õ", "ö", "÷", "ù"},
            {"ó", "ï", "ñ", "ò", "ô", "¦", "ø", "õ", "ö", "÷", "ù"},

            {"ý", "ú", "û", "ü", "þ"},
            {"ý", "ú", "û", "ü", "þ"}

            };
        for (int i = 0; i < uniChar.length; i++) {
            for (int j = 0; j < uniChar[i].length; j++) {
                s = s.replaceAll(uniChar[i][j], Character.toString(arrChar[i]));
            }
        }
        return s;
    }
public String removeVIQRSign(String s) {
        CharSequence[] arrChar = {"a", "A",  "d", "D", "e", "E", "i", "I","o", "O", "u", "U", "y", "Y", ".", "?"};
        CharSequence[][] uniChar = {
            {"a('", "a(`", "a(?", "a(~", "a(.", "a^'", "a^`", "a^?", "a^~", "a^.", "a'", "a`", "a?", "a~", "a.", "a(", "a^"},
            {"A('", "A(`", "A(?", "A(~", "A(.", "A^'", "A^`", "A^?", "A^~", "A^.", "A'", "A`", "A?", "A~", "A.", "A(", "A^"},

            {"dd"},
            {"DD"},

            {"e^'", "e^`", "e^?", "e^~", "e^.", "e'", "e`", "e?", "e~", "e.", "e^"},
            {"E^'", "E^`", "E^?", "E^~", "E^.", "E'", "E`", "E?", "E~", "E.", "E^"},

          
            { "i'", "i`", "i?", "i~", "i."},
            { "I'", "I`", "I?", "I~", "I."},


            {"o+'", "o+`", "o+?", "o+~", "o+.", "o^'", "o^`", "o^?", "o^~", "o^.", "o'", "o`", "o?", "o~", "o.", "o+", "o^"},
            {"O+'", "O+`", "O+?", "O+~", "O+.", "O^'", "o^`", "O^?", "O^~", "O^.", "O'", "O`", "O?", "O~", "O.", "O+", "O^"},

            {"u+'", "u+`", "u+?", "u+~", "u+.", "u'", "u`", "u?", "u~", "u.", "u+",},
            {"U+'", "U+`", "U+?", "U+~", "U+.", "U'", "U`", "U?", "U~", "U.", "U+"},

            {"y'", "y`", "y?", "y~", "y."},
            {"Y'", "Y`", "Y?", "Y~", "Y."},
            {"\\."},
             {"\\?"},
            };

        for (int i = 0; i < uniChar.length; i++) {
            for (int j = 0; j < uniChar[i].length; j++) {
                CharSequence src = uniChar[i][j].toString();
                CharSequence des = arrChar[i].toString();
                s = s.replace(src, des);
            }
        }
        return s;
    }
}
