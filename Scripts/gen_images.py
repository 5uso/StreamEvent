from PIL import Image

colors = ["gray", "white", "pink", "purple", "blue", "cyan", "light_blue", "green", "lime", "yellow", "orange", "red"]
color_indexes = [7,15,13,5,9,3,11,2,10,14,6,4]

for c, idx in zip(colors, color_indexes):
    img = Image.new(mode = "RGBA", size = (16, 16), color = (idx, 0, 0, 1))
    img.save(f'{c}.png')