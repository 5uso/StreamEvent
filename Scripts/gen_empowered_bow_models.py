times = [str(x/60 + 0.65)[:6] for x in range(22)]

for i, t in enumerate(times):
    filename = f'bow_pulling_1_{i}' if i < 15 else f'bow_pulling_2_{i}'
    print(f'''        {{"predicate":{{"custom_model_data":1,"pulling":1,"pull":{t}}},"model":"suso:item/bow/empowered/{filename}"}},''')
    with open(f'./{filename}.json', 'w') as f:
        f.write(f'''{{"parent":"suso:item/bow/empowered/base","textures":{{"layer0":"suso:item/bow/empowered/{filename}"}}}}''')

