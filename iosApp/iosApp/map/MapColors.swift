import Foundation
import SwiftUI

let mapBackgroundLight = UIColor(red:0.98, green:0.98, blue: 0.98, alpha: 1.0)
let labelColorLight = UIColor(red:0.0, green:0.0, blue: 0.0, alpha: 1.0)
let labelHaloColorLight = UIColor(red:1.0, green:1.0, blue: 1.0, alpha: 1.0)
let mapColorsLight = [
    "streets": UIColor(red:0.89411765, green:0.8862745, blue: 0.8862745, alpha: 1.0),
    "bars": UIColor(red:0.6431373, green:0.7411765, blue: 0.96862745, alpha: 1.0),
    "candy-stalls": UIColor(red:0.69803923, green:0.6117647, blue: 0.85490197, alpha: 1.0),
    "expo-stalls": UIColor(red:0.46666667, green:0.46666667, blue: 0.46666667, alpha: 1.0),
    "food-stalls": UIColor(red:0.972549, green:0.92941177, blue: 0.69803923, alpha: 1.0),
    "game-stalls": UIColor(red:0.78039217, green:0.9764706, blue: 0.54901963, alpha: 1.0),
    "miscs": UIColor(red:0.827451, green:0.68235296, blue: 0.5921569, alpha: 1.0),
    "fill_public_transport": UIColor(red:0.827451, green:0.68235296, blue: 0.5921569, alpha: 1.0),
    "restaurants": UIColor(red:0.827451, green:0.68235296, blue: 0.5921569, alpha: 1.0),
    "buildings": UIColor(red:0.827451, green:0.68235296, blue: 0.5921569, alpha: 1.0),
    "restrooms": UIColor(red:0.6745098, green:0.078431375, blue: 0.34117648, alpha: 1.0),
    "rides": UIColor(red:0.52156866, green:0.8235294, blue: 0.84705883, alpha: 1.0),
    "seller-stalls": UIColor(red:0.54509807, green:0.6156863, blue: 0.00390619, alpha: 1.0)
]
let mapSymbolBackgroundLight = [
    "bar": UIColor(red:0.12109375, green:0.2890625, blue: 0.67578125, alpha: 1.0),
    "candy_stall": UIColor(red:0.31640625, green:0.12109375, blue: 0.67578125, alpha: 1.0),
    "expo": UIColor(red:0.67578125, green:0.12109375, blue: 0.12109375, alpha: 1.0),
    "food_stall": UIColor(red:0.67578125, green:0.58984375, blue: 0.12109375, alpha: 1.0),
    "game_stall": UIColor(red:0.421875, green:0.67578125, blue: 0.12109375, alpha: 1.0),
    "misc": UIColor(red:0.67578125, green:0.33203125, blue: 0.12109375, alpha: 1.0),
    "parking": UIColor(red:0.171875, green:0.67578125, blue: 0.12109375, alpha: 1.0),
    "restaurant": UIColor(red:0.67578125, green:0.33203125, blue: 0.12109375, alpha: 1.0),
    "restroom": UIColor(red:0.67578125, green:0.12109375, blue: 0.3671875, alpha: 1.0),
    "ride": UIColor(red:0.12109375, green:0.63671875, blue: 0.67578125, alpha: 1.0),
    "seller_stall": UIColor(red:0.12109375, green:0.20703125, blue: 0.67578125, alpha: 1.0),
    "station": UIColor(red:0.67578125, green:0.33203125, blue: 0.12109375, alpha: 1.0),
    "platform": UIColor(red:0.67578125, green:0.33203125, blue: 0.12109375, alpha: 1.0),
    "taxi": UIColor(red:0.67578125, green:0.33203125, blue: 0.12109375, alpha: 1.0),
    "entrance": UIColor(red:0.67578125, green:0.33203125, blue: 0.12109375, alpha: 1.0),
]

let mapBackgroundDark = UIColor(red:0.1, green:0.1, blue: 0.1, alpha: 1.0)
let labelColorDark = UIColor(red:1.0, green:1.0, blue: 1.0, alpha: 1.0)
let labelHaloColorDark = UIColor(red:0.0, green:0.0, blue: 0.0, alpha: 1.0)
let mapColorsDark = [
    "streets": UIColor(red:0.36862746, green:0.3529412, blue: 0.36078432, alpha: 1.0),
    "bars": UIColor(red:0.047058824, green:0.2, blue: 0.5529412, alpha: 1.0),
    "candy-stalls": UIColor(red:0.25882354, green:0.16470589, blue: 0.43529412, alpha: 1.0),
    "expo-stalls": UIColor(red:0.3019608, green:0.3019608, blue: 0.3019608, alpha: 1.0),
    "food-stalls": UIColor(red:0.54901963, green:0.47058824, blue: 0.050980393, alpha: 1.0),
    "game-stalls": UIColor(red:0.32156864, green:0.5686275, blue: 0.03137255, alpha: 1.0),
    "miscs": UIColor(red:0.42352942, green:0.27058825, blue: 0.1764706, alpha: 1.0),
    "fill_public_transport": UIColor(red:0.42352942, green:0.27058825, blue: 0.1764706, alpha: 1.0),
    "restaurants": UIColor(red:0.42352942, green:0.27058825, blue: 0.1764706, alpha: 1.0),
    "buildings": UIColor(red:0.42352942, green:0.27058825, blue: 0.1764706, alpha: 1.0),
    "restrooms": UIColor(red:0.5372549, green:0.0627451, blue: 0.27058825, alpha: 1.0),
    "rides": UIColor(red:0.14509805, green:0.43137255, blue: 0.45490196, alpha: 1.0),
    "seller-stalls": UIColor(red:0.003921569, green:0.09803922, blue: 0.0023375626, alpha: 1.0)
]
let mapSymbolBackgroundDark = [
    "bar": UIColor(red:0.12109375, green:0.2890625, blue: 0.67578125, alpha: 1.0),
    "candy_stall": UIColor(red:0.31640625, green:0.12109375, blue: 0.67578125, alpha: 1.0),
    "expo": UIColor(red:0.67578125, green:0.12109375, blue: 0.12109375, alpha: 1.0),
    "food_stall": UIColor(red:0.67578125, green:0.58984375, blue: 0.12109375, alpha: 1.0),
    "game_stall": UIColor(red:0.421875, green:0.67578125, blue: 0.12109375, alpha: 1.0),
    "misc": UIColor(red:0.67578125, green:0.33203125, blue: 0.12109375, alpha: 1.0),
    "parking": UIColor(red:0.171875, green:0.67578125, blue: 0.12109375, alpha: 1.0),
    "restaurant": UIColor(red:0.67578125, green:0.33203125, blue: 0.12109375, alpha: 1.0),
    "restroom": UIColor(red:0.67578125, green:0.12109375, blue: 0.3671875, alpha: 1.0),
    "ride": UIColor(red:0.12109375, green:0.63671875, blue: 0.67578125, alpha: 1.0),
    "seller_stall": UIColor(red:0.12109375, green:0.20703125, blue: 0.67578125, alpha: 1.0),
    "station": UIColor(red:0.67578125, green:0.33203125, blue: 0.12109375, alpha: 1.0),
    "platform": UIColor(red:0.67578125, green:0.33203125, blue: 0.12109375, alpha: 1.0),
    "taxi": UIColor(red:0.67578125, green:0.33203125, blue: 0.12109375, alpha: 1.0),
    "entrance": UIColor(red:0.67578125, green:0.33203125, blue: 0.12109375, alpha: 1.0),
]
