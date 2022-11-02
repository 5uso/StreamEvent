from PIL import Image

for i in range(22):
    img = Image.open('bow_pulling_1.png' if i < 15 else 'bow_pulling_2.png')
    pixel = (i, 0, 0, 2)
    img.putpixel((0, 0), pixel)
    img.putpixel((0, img.size[1]-1), pixel)
    img.putpixel((img.size[0]-1, 0), pixel)
    img.putpixel((img.size[0]-1, img.size[1]-1), pixel)
    img.save(f'bow_pulling_1_{i}.png' if i < 15 else f'bow_pulling_2_{i}.png')

