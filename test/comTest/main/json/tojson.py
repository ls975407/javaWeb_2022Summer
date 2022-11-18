import os
import sys

'''
_pathLPicture = json.getString("PathLPicture");
_pathLPicture getString PathLPicture 
_dirUser = json.
_defaultPgFrame = json.
'''

def parseLine(line):
    eles = line.split()
    p0 = eles[0]
    index = line.find("json.")
    if index < 0:
        raise Exception(f"index < 0 {line=}")
    line_t = line[index+5:].split("\"");
    if len(line_t) < 2:
        raise Exception(f"len(line_t) < 2 {line=} {line_t=}")
    p1 = line_t[0][:-1]
    p2 = line_t[1]
    return (p0, p1, p2)

buf = []
with open("SettingAd.java", "r", encoding="UTF-8") as reader:
    while(line := reader.readline()):
        line_t = line.strip()
        if line_t.startswith("_dirUser = json."):
            buf.append(line_t)
            break;
    while(line := reader.readline()):
        line_t = line.strip()
        if line_t.find("json.") < 0:
            continue
        buf.append(line_t)
        if line_t.startswith("_defaultPgFrame = json."):
            break;

objs = []
for line in buf:
    p0, p1, p2 = parseLine(line)
    if p1 == "getString":
        mark = "\"\""
    elif p1 == "getInt":
        mark = "0"
    elif p1 == "getLong":
        mark = "0"
    elif p1 == "getJSONArray":
        mark = "[]"
    elif p1 == "getDouble":
        mark = "0.0"
    else:
        raise Exception("unable to catch " + p1)
    objs.append((f"\"{p2}\"", mark))
    
with open("IniSetting.json", "w", encoding="UTF-8") as writer:
    writer.write("{\n");
    for (key, value) in objs[:-1]:
        writer.write("\t");
        writer.write(f"{key}: {value},");
        writer.write("\n");
    (key, value) = objs[-1]
    writer.write("\t");
    writer.write(f"{key}: {value}");
    writer.write("\n");
    writer.write("}\n");