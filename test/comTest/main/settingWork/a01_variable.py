import os
import shutil
import sys
from abc import ABC, abstractmethod
'''
public T $name(String $notImp);
var: _t$name
key: T$name
method:
    switch(T):
        String: _iniTS2S
        int: _iniTS2I
        double: _iniTS2F
getMethod: public T $name(String $notImp) { return $var; }
jsonMethod: $var = $method(json.getJSONArray($key));
variable: T $var;

public T get$name();
var: _lowercase($name[0])$name[1:]
key: $name
method:
    switch(T):
        String: getString
        int: getInt
        double: getDouble
        long: getLong
getMethod: public T get$name() { return $var; }
jsonMethod: $var = json.$method($key);
variable: T $var;
'''

class StrMaker(ABC):
    def __init__(self):
        pass
    @abstractmethod
    def toString(self):
        pass
    @abstractmethod
    def toJson(self):
        pass
    @abstractmethod
    def toVariable(self):
        pass
class StrNoWork(StrMaker):
    def __init__(self, content):
        self.content = content
    def toString(self):
        return self.content
    def toJson(self):
        return ""
    def toVariable(self):
        return ""
class StrMethod(StrMaker):
    def __init__(self, variable, key, type_):
        self.variable = variable
        self.key = key
        self.type_ = type_
        self.methodString = self.getMethodString(type_)
    @abstractmethod
    def getMethodString(self, type_):
        pass
class StrMethodSingle(StrMethod):
    def __init__(self, eles):
        type_ = eles[1]
        name_t = eles[2].split("(")[0][3:]
        self.name = name_t
        # print(eles)
        # print(name_t)
        variable = "_" + name_t[0].lower() + name_t[1:]
        key = "\"" + self.name + "\""
        self.name = eles[2].split("(")[0]
        super().__init__(variable, key, type_)
    def getMethodString(self, type_):
        if type_ == "String":
            return "json.getString"
        elif type_ == "int":
            return "json.getInt"
        elif type_ == "long" or type_ == "Long":
            return "json.getLong"
        elif type_ == "double":
            return "json.getDouble"
        elif type_ == "String[]":
            return "_iniAS2S(json.getJSONArray("
        print(f"{self.name=}")
        raise Exception(f"type_ not find {self.type_=}")
    def toString(self):
        return f"\tpublic {self.type_} {self.name}() {{ return {self.variable}; }}\n"
    def toJson(self):
        if self.type_ == "String[]":
            return f"\t\t{self.variable} = {self.methodString}{self.key}));\n"
        return f"\t\t{self.variable} = {self.methodString}({self.key});\n"
    def toVariable(self):
        if self.type_ == "Long" or self.type_ == "String" or self.type_ == "String[]":
            return f"\tprivate {self.type_} {self.variable} = null;\n"
        else:
            return f"\tprivate {self.type_} {self.variable} = 0;\n"
class StrMethodDefault(StrMethod):
    def __init__(self, eles):
        type_ = "String"
        name_t = eles[2].split("(")[0][3:]
        self.name = name_t
        # print(eles)
        # print(name_t)
        variable = "_" + name_t[0].lower() + name_t[1:]
        key =  "\"" + self.name + "\""
        super().__init__(variable, key, type_)
    def getMethodString(self, type_):
        return ""
    def toString(self):
        return f"\tpublic FrameM get{self.name}() {{ return findFrameObj({self.variable}); }}\n"
    def toJson(self):
        # return f"\t{self.variable} = json.{self.methodString}({self.key});\n"  
        return f"\t\t{self.variable} = json.getString({self.key});\n"  
    def toVariable(self):
        return f"\tprivate String {self.variable} = null;\n"
class StrMethodTable(StrMethod):
    def __init__(self, eles):
        type_ = eles[1]
        self.name = eles[2].split("(")[0]
        variable = f"_T{self.name}"
        key =  "\"" + self.name[0].upper() + self.name[1:] + "\""
        super().__init__(variable, key, type_)
    def getMethodString(self, type_):
        if type_ == "String":
            self.mapType = self.type_
            return "_iniTS2S"
        elif type_ == "int":
            self.mapType = "Integer"
            return "_iniTS2I"
        elif type_ == "FrameM":
            self.mapType = self.type_
            return "_iniTS2F"
        print(f"{self.name=}")
        raise Exception(f"T type_ not find {self.type_=}")
    def toString(self):
        return f"\tpublic {self.type_} {self.name}(String key) {{ return {self.variable}.get(key); }}\n"
    def toJson(self):
        # return f"\t{self.variable} = json.{self.methodString}({self.key});\n"  
        return f"\t\t{self.variable} = {self.methodString}(json.getJSONArray({self.key}));\n"  
    def toVariable(self):
        return f"\tprivate Map<String, {self.mapType}> {self.variable} = null;\n"
class StrMethodTableInt(StrMethod):
    def __init__(self, eles):
        type_ = eles[1]
        self.name = eles[2].split("(")[0]
        variable = f"_T{self.name}"
        key =  "\"" + self.name[0].upper() + self.name[1:] + "\""
        super().__init__(variable, key, type_)
    def getMethodString(self, type_):
        if type_ == "String":
            self.mapType = self.type_
            return "_iniTI2S"
        elif type_ == "int":
            self.mapType = "Integer"
            return "_iniTI2I"
        print(f"{self.name=}")
        raise Exception(f"T type_ not find {self.type_=}")
    def toString(self):
        return f"\tpublic {self.type_} {self.name}(int key) {{ return {self.variable}.get(key); }}\n"
    def toJson(self):
        # return f"\t{self.variable} = json.{self.methodString}({self.key});\n"  
        return f"\t\t{self.variable} = {self.methodString}(json.getJSONArray({self.key}));\n"  
    def toVariable(self):
        return f"\tprivate Map<Integer, {self.mapType}> {self.variable} = null;\n"
    


def eleCreate(line):
    line_1 = line.strip()
    if not line_1.startswith("public abstract ") or not line_1.endswith(");"):
        return StrNoWork(line)
    eles = line_1.split()
    eles = [eles[0]] + eles[2:]
    if eles[2].startswith("getFontTypes") or eles[2].startswith("getFontSizes"):
        return StrMethodTableInt(eles)
    if line_1.find("()") < 0:
        return StrMethodTable(eles)
    # print(f"{line_1=}")
    
    if eles[2].startswith("getDefault"):
        return StrMethodDefault(eles)
    return StrMethodSingle(eles)
    

_strMakerList = []
with open("SettingAd.java", "r", encoding="UTF-8") as reader:
    while line := reader.readline():
        _strMakerList.append(eleCreate(line))

with open("01output.java", "w", encoding="UTF-8") as writer:
    for strMaker in _strMakerList:
        writer.write(strMaker.toString())
    
with open("02output.java", "w", encoding="UTF-8") as writer:
    # writer.write("\tprivate void parseJson(JSONObject json) throws JSONException {\n")
    for strMaker in _strMakerList:
        writer.write(strMaker.toJson())
    # writer.write("\t}\n")

with open("03output.java", "w", encoding="UTF-8") as writer:
    for strMaker in _strMakerList:
        writer.write(strMaker.toVariable())

