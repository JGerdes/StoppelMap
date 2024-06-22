import Foundation
import SwiftUI

extension Font {

    /// Create a font with the extra large title text style.
    public static var extraLargeTitleSlab: Font {
        return Font.custom("RobotoSlab-Light", size: UIFont.preferredFont(forTextStyle: .largeTitle).pointSize * 1.4)
    }

   /// Create a font with the large title text style.
   public static var largeTitleSlab: Font {
       return Font.custom("RobotoSlab-Regular", size: UIFont.preferredFont(forTextStyle: .largeTitle).pointSize)
   }

   /// Create a font with the title text style.
   public static var titleSlab: Font {
       return Font.custom("RobotoSlab-Regular", size: UIFont.preferredFont(forTextStyle: .title1).pointSize)
   }
    
    /// Create a font with the title2 text style.
   public static var title2Slab: Font {
       return Font.custom("RobotoSlab-Regular", size: UIFont.preferredFont(forTextStyle: .title2).pointSize)
   }

   /// Create a font with the headline text style.
   public static var headlineSlab: Font {
       return Font.custom("RobotoSlab-Regular", size: UIFont.preferredFont(forTextStyle: .headline).pointSize)
   }

   /// Create a font with the subheadline text style.
   public static var subheadlineSlab: Font {
       return Font.custom("RobotoSlab-Light", size: UIFont.preferredFont(forTextStyle: .subheadline).pointSize)
   }

   /// Create a font with the body text style.
   public static var bodySlab: Font {
          return Font.custom("RobotoSlab-Regular", size: UIFont.preferredFont(forTextStyle: .body).pointSize)
      }

   /// Create a font with the callout text style.
   public static var calloutSlab: Font {
          return Font.custom("RobotoSlab-Regular", size: UIFont.preferredFont(forTextStyle: .callout).pointSize)
      }

   /// Create a font with the footnote text style.
   public static var footnoteSlab: Font {
          return Font.custom("RobotoSlab-Regular", size: UIFont.preferredFont(forTextStyle: .footnote).pointSize)
      }

   /// Create a font with the caption text style.
   public static var captionSlab: Font {
          return Font.custom("RobotoSlab-Regular", size: UIFont.preferredFont(forTextStyle: .caption1).pointSize)
      }

   public static func system(size: CGFloat, weight: Font.Weight = .regular, design: Font.Design = .default) -> Font {
       var font = "RobotoSlab-Regular"
       switch weight {
       case .bold: font = "RobotoSlab-Bold"
       case .heavy: font = "RobotoSlab-Bold"
       case .light: font = "RobotoSlab-Light"
       case .medium: font = "RobotoSlab-Regular"
       case .semibold: font = "RobotoSlab-Regular"
       case .thin: font = "RobotoSlab-Thin"
       case .ultraLight: font = "RobotoSlab-Thin"
       default: break
       }
       return Font.custom(font, size: size)
   }
}
